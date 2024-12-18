package org.example.marathon.service;

import org.example.marathon.domain.po.Participate;

public interface ParticipateService {
    boolean addParticipate(Participate participate);

    boolean deleteParticipate(Integer playerId, Integer eventId);
}
