package com.inu.inunity.domain.advertise;

import com.inu.inunity.common.exception.ExceptionMessage;
import com.inu.inunity.common.exception.NotFoundElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdvertiseService {

    private final AdvertiseRepository advertiseRepository;

    @Transactional
    public Page<ResponseAdvertise> getAdvertises(Pageable pageable){
        Page<Advertise> advertises = advertiseRepository.findAllByOrderByUpdateAtDesc(pageable);

        return advertises.map(advertise -> ResponseAdvertise
                .of(advertise.getId(), advertise.getTitle(), advertise.getContent(), advertise.getUrl(), advertise.getCreateAt(),
                        advertise.getUpdateAt()));
    }

    @Transactional
    public ResponseAdvertise getAdvertise(Long advertiseId){
        Advertise advertise = advertiseRepository
                .findById(advertiseId).orElseThrow(()->new NotFoundElementException(ExceptionMessage.ARTICLE_NOT_FOUND));

        return ResponseAdvertise.of(advertise.getId(), advertise.getTitle(), advertise.getContent(), advertise.getUrl(),
                advertise.getCreateAt(), advertise.getUpdateAt());
    }

    @Transactional
    public Long createAdvertise(RequestCreateUpdateAdvertise requestCreateUpdateAdvertise){
        Advertise advertise = Advertise.of(requestCreateUpdateAdvertise.title(), requestCreateUpdateAdvertise.content(), requestCreateUpdateAdvertise.url());

        advertiseRepository.save(advertise);
        return advertise.getId();
    }

    @Transactional
    public Long editAdvertise(Long advertiseId, RequestCreateUpdateAdvertise requestCreateUpdateAdvertise){
        Advertise advertise = advertiseRepository
                .findById(advertiseId).orElseThrow(()->new NotFoundElementException(ExceptionMessage.ARTICLE_NOT_FOUND));

        advertise.editAdvertise(requestCreateUpdateAdvertise.title(), requestCreateUpdateAdvertise.content(), requestCreateUpdateAdvertise.url());

        return advertiseId;
    }

    @Transactional
    public void deleteAdvertise(Long advertiseId){
        advertiseRepository.deleteById(advertiseId);
    }
}
