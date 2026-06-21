package org.example.service;

import org.example.dto.PackageCreateRequest;
import org.example.dto.PackageResponse;
import org.example.dto.PackageUpdateRequest;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Package;
import org.example.model.enums.DurationType;
import org.example.model.enums.PackageType;
import org.example.repository.PackageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PackageService {

    private final PackageRepository packageRepository;

    public PackageService(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    public List<PackageResponse> findAll() {
        return packageRepository.findAll().stream().map(DtoMapper::toPackageResponse).toList();
    }

    public PackageResponse findById(Long id) {
        return DtoMapper.toPackageResponse(findEntityById(id));
    }

    public PackageResponse create(PackageCreateRequest request) {
        Package carePackage = new Package();
        carePackage.setName(request.name());
        carePackage.setType(parsePackageType(request.type()));
        carePackage.setPrice(request.price());
        carePackage.setDescription(request.description());
        carePackage.setDurationType(parseDurationType(request.durationType()));
        carePackage.setStartDatetime(request.startDatetime());
        carePackage.setEndDatetime(request.endDatetime());
        return DtoMapper.toPackageResponse(packageRepository.save(carePackage));
    }

    public PackageResponse update(Long id, PackageUpdateRequest request) {
        Package carePackage = findEntityById(id);
        if (request.name() != null) {
            carePackage.setName(request.name());
        }
        if (request.type() != null) {
            carePackage.setType(parsePackageType(request.type()));
        }
        if (request.price() != null) {
            carePackage.setPrice(request.price());
        }
        if (request.description() != null) {
            carePackage.setDescription(request.description());
        }
        if (request.durationType() != null) {
            carePackage.setDurationType(parseDurationType(request.durationType()));
        }
        if (request.startDatetime() != null) {
            carePackage.setStartDatetime(request.startDatetime());
        }
        if (request.endDatetime() != null) {
            carePackage.setEndDatetime(request.endDatetime());
        }
        return DtoMapper.toPackageResponse(packageRepository.save(carePackage));
    }

    public void delete(Long id) {
        packageRepository.delete(findEntityById(id));
    }

    public Package findEntityById(Long id) {
        return packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found."));
    }

    private PackageType parsePackageType(String value) {
        return PackageType.valueOf(value.trim().toUpperCase());
    }

    private DurationType parseDurationType(String value) {
        return DurationType.valueOf(value.trim().toUpperCase());
    }
}
