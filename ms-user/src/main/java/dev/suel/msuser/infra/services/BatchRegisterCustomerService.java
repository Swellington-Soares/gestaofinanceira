package dev.suel.msuser.infra.services;

import com.google.common.hash.Hashing;
import dev.suel.msuser.application.gateway.BatchRegisterCustomerPort;
import dev.suel.msuser.domain.FileUploadStatus;
import dev.suel.msuser.domain.entity.Customer;
import dev.suel.msuser.domain.valueobject.Email;
import dev.suel.msuser.domain.valueobject.Password;
import dev.suel.msuser.domain.valueobject.PersonName;
import dev.suel.msuser.dto.FileUploadResponse;
import dev.suel.msuser.infra.mapper.CustomerMapper;
import dev.suel.msuser.infra.persistence.repository.CustomerEntityRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BatchRegisterCustomerService implements BatchRegisterCustomerPort {


    private final Map<String, FileUploadStatus> fileUploadStatusMap = new HashMap<>();
    private final CustomerEntityRepository customerEntityRepository;
    private final CustomerMapper customerMapper;

    @Override
    public FileUploadStatus checkStatus(String id) {
        return fileUploadStatusMap.get(id);
    }

    @Override
    public String getHashCode(BufferedInputStream stream) throws IOException {
        stream.mark(Integer.MAX_VALUE);
        String id = Hashing.sha256().hashBytes(stream.readAllBytes()).toString();
        stream.reset();
        return id;
    }

    @Override
    @Async
    public void process(FileUploadStatus fileUploadStatus, BufferedInputStream stream) {

        FileUploadStatus oldFile = fileUploadStatusMap.get(fileUploadStatus.getId());

        if (oldFile != null && !oldFile.isFinished())
            throw new IllegalStateException("O mesmo arquivo já se encontra na fila de processamento.");

        fileUploadStatusMap.put(fileUploadStatus.getId(), fileUploadStatus);

        try (Workbook workbook = WorkbookFactory.create(stream)) {

            if (workbook.getNumberOfSheets() == 0) {
                fileUploadStatus.setStatus(FileUploadStatus.Status.STATUS_FINISHED);
                fileUploadStatus.addError("Arquivo inválido.");
                return;
            }

            Sheet sheet = workbook.getSheetAt(0);

            if (sheet.getPhysicalNumberOfRows() <= 1) {
                fileUploadStatus.setStatus(FileUploadStatus.Status.STATUS_FINISHED);
                fileUploadStatus.addError("Nenhum registro encontrado.");
                return;
            }


            Set<Customer> customers = new HashSet<>();
            DataFormatter formatter = new DataFormatter();

            fileUploadStatus.setTotalItems((long) sheet.getPhysicalNumberOfRows() - 1);

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                try {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;
                    String name = formatter.formatCellValue(row.getCell(0));
                    String email = formatter.formatCellValue(row.getCell(1));
                    String password = formatter.formatCellValue(row.getCell(2));

                    if (name.isBlank() || email.isBlank() || password.isBlank()) continue;
                    ;

                    Customer customer = Customer.builder()
                            .email(Email.of(email))
                            .name(PersonName.of(name))
                            .password(Password.of(password))
                            .build();

                    customers.add(customer);

                    fileUploadStatus.increaseProcessedItems();

                } catch (Exception e) {
                    fileUploadStatus.addError(String.format("Registro #%d: %s", i, e.getMessage()));
                }
            }

            if (!customers.isEmpty()) {
                customerEntityRepository.saveAll(
                        customers.stream().map(customerMapper::modelToEntity).toList()
                );
            }

            fileUploadStatus.setStatus(FileUploadStatus.Status.STATUS_FINISHED);

        } catch (Exception e) {
            fileUploadStatus.setStatus(FileUploadStatus.Status.STATUS_ERROR);
            fileUploadStatus.addError(e.getMessage());
        }
    }
}
