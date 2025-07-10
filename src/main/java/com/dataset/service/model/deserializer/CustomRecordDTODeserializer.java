package com.dataset.service.model.deserializer;

import com.dataset.service.model.DepartmentDTO;
import com.dataset.service.model.EmployeeDTO;
import com.dataset.service.model.RecordDTO;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class CustomRecordDTODeserializer extends JsonDeserializer<RecordDTO> {

    @Override
    public RecordDTO deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper objectMapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode jsonNode = objectMapper.readTree(jsonParser);
        if (jsonNode.has("departmentName")) {
            return objectMapper.treeToValue(jsonNode, DepartmentDTO.class);
        }
        if (jsonNode.has("age")) {
            return objectMapper.treeToValue(jsonNode, EmployeeDTO.class);
        }
        throw new RuntimeException("Could not deserialize payload");
    }
}
