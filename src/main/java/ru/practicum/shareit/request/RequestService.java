package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.Request;
import ru.practicum.shareit.request.dto.RequestOnItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseOnItemRequestDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestService {
    private final RequestRepository requestRepository;
    private final ItemService itemService;
    private final UserService userService;

    public ResponseOnItemRequestDto createRequest(RequestOnItemRequestDto requestDto, long requestorId) {
        Request request = RequestMapper.toRequest(requestDto);
        request.setRequestor(UserMapper.toUser(userService.getUser(requestorId)));
        return RequestMapper.toResponseOnRequest(requestRepository.save(request));
    }

    public List<ResponseOnItemRequestDto> getRequests(long userId) {
        List<ResponseOnItemRequestDto> requests = RequestMapper.toResponseOnRequestList(requestRepository.findByRequestor(UserMapper.toUser(userService.getUser(userId))));

        for (ResponseOnItemRequestDto r : requests) {
            r.setItems(itemService.findItemByRequest(r.getId()));
        }

        return requests;
    }

    public List<ResponseOnItemRequestDto> getRequests(long userId, int from, int size) {
        if (from < 0 || size < 0) {
            throw new ValidException("Отрицательное число from или size");
        }

        List<ResponseOnItemRequestDto> requestDtos = RequestMapper.toResponseOnRequestList(requestRepository.findRequestsByRequestorIsNotOrderByCreatedDesc
                (UserMapper.toUser(userService.getUser(userId)),
                        PageRequest.of(from / size, size, Sort.by("created"))).toList());
        for (ResponseOnItemRequestDto r : requestDtos){
            r.setItems(itemService.findItemByRequest(r.getId()));
        }

        return requestDtos;
    }

    public ResponseOnItemRequestDto getRequestById(long requestId, long userId) {
        if (!userService.validation(userId)){
            throw new ValidationException("Пользователь не найден");
        }
        ResponseOnItemRequestDto requestDto = RequestMapper.toResponseOnRequest(requestRepository.findById(requestId).orElseThrow(() -> new ValidationException("Запрос не существует")));
        requestDto.setItems(itemService.findItemByRequest(requestId));
        return requestDto;
    }
}
