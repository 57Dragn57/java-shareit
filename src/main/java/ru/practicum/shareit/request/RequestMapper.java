package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.Request;
import ru.practicum.shareit.request.dto.RequestOnItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseOnItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {
    public static Request toRequest(RequestOnItemRequestDto requestDto) {
        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setDescription(requestDto.getDescription());

        return request;
    }

    public static ResponseOnItemRequestDto toResponseOnRequest(Request request) {
        return ResponseOnItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requestor(request.getRequestor())
                .created(request.getCreated())
                .build();
    }

    public static List<ResponseOnItemRequestDto> toResponseOnRequestList(List<Request> requests) {
        return requests.stream().map(RequestMapper::toResponseOnRequest).collect(Collectors.toList());
    }
}
