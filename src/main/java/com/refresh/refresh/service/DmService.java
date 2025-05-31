package com.refresh.refresh.service;

import com.refresh.refresh.entity.Dm;
import com.refresh.refresh.repository.DmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DmService {

    @Autowired
    private DmRepository dmRepository;

    /**
     * 모든 DM 정보를 가져오는 메서드
     * @return List<Dm>
     */
    public List<Dm> getAllDms() {
        return dmRepository.findAll();
    }

    /**
     * 특정 ID의 DM 정보를 가져오는 메서드
     * @param id DM ID
     * @return Dm
     */
    public Dm getDmById(int id) {
        return dmRepository.findById(id).orElseThrow(() -> new RuntimeException("DM not found"));
    }

    /**
     * 새로운 DM 정보를 생성하는 메서드
     * @param dm DM 엔티티
     * @return Dm
     */
    public Dm createDm(Dm dm) {
        return dmRepository.save(dm);
    }

    /**
     * 기존 DM 정보를 업데이트하는 메서드
     * @param dm DM 엔티티
     * @return Dm
     */
    public Dm updateDm(Dm dm) {
        if (!dmRepository.existsById(dm.getDmId())) {
            throw new RuntimeException("DM not found");
        }
        return dmRepository.save(dm);
    }

    /**
     * 특정 ID의 DM 정보를 삭제하는 메서드
     * @param id DM ID
     */
    public void deleteDm(int id) {
        if (!dmRepository.existsById(id)) {
            throw new RuntimeException("DM not found");
        }
        dmRepository.deleteById(id);
    }
}
