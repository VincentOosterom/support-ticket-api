package nl.ticketsystem.mapper;

import java.util.List;

public interface DTOMapper <RESPONSE, REQUEST, ENTITY>{
    RESPONSE mapToDto(ENTITY model);
    List<RESPONSE>mapToDto(List<ENTITY> models);
    ENTITY mapToEntity(REQUEST request);
}
