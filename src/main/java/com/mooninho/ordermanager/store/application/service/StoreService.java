package com.mooninho.ordermanager.store.application.service;

import com.mooninho.ordermanager.exception.exception.owner.OwnerNotFoundException;
import com.mooninho.ordermanager.exception.exception.store.DuplicateStoreNameException;
import com.mooninho.ordermanager.owner.domain.repository.OwnerRepository;
import com.mooninho.ordermanager.owner.domain.vo.Username;
import com.mooninho.ordermanager.store.application.dto.request.CreateStoreRequestDto;
import com.mooninho.ordermanager.store.domain.repository.StoreRepository;
import com.mooninho.ordermanager.store.domain.vo.StoreName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final OwnerRepository ownerRepository;

    @Transactional
    public void createStore(CreateStoreRequestDto createStoreRequestDto, String userId) {

        checkDuplicateStoreName(createStoreRequestDto.getStoreName());

        storeRepository.save(createStoreRequestDto.toEntity(getOwnerId(userId)));
    }

    @Transactional(readOnly = true)
    protected void checkDuplicateStoreName(String storeName) {

        boolean existStoreName = storeRepository.isExistStoreName(StoreName.of(storeName));
        if (existStoreName) {
            throw new DuplicateStoreNameException();
        }
    }

    @Transactional(readOnly = true)
    protected Long getOwnerId(String username) {

        return ownerRepository.getOwnerId(Username.of(username))
                .orElseThrow(OwnerNotFoundException::new);
    }
}
