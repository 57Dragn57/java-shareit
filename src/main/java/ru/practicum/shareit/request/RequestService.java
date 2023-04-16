package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.Request;
import ru.practicum.shareit.request.dto.RequestOnItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseOnItemRequestDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        List<Request> requests = requestRepository.findByRequestor(UserMapper.toUser(userService.getUser(userId)));

        return addItemsInRequests(requests);
    }

    public List<ResponseOnItemRequestDto> getRequests(long userId, int from, int size) {
        List<Request> requests = requestRepository.findRequestsByRequestorIsNotOrderByCreatedDesc
                (UserMapper.toUser(userService.getUser(userId)),
                        PageRequest.of(from / size, size, Sort.by("created"))).toList();

        return addItemsInRequests(requests);
    }

    public ResponseOnItemRequestDto getRequestById(long requestId, long userId) {
        if (!userService.validation(userId)) {
            throw new ValidationException("Пользователь не найден");
        }
        ResponseOnItemRequestDto requestDto = RequestMapper.toResponseOnRequest(
                requestRepository.findById(requestId).orElseThrow(() -> new ValidationException("Запрос не существует")));
        requestDto.setItems(itemService.findItemByRequest(requestId));
        return requestDto;
    }

    private List<ResponseOnItemRequestDto> addItemsInRequests(List<Request> requests) {
        Map<Request, List<Item>> map = itemService.findItemByRequest(requests);

        List<ResponseOnItemRequestDto> requestDtos = new ArrayList<>();
        for (Request r : requests) {
            ResponseOnItemRequestDto requestDto = RequestMapper.toResponseOnRequest(r);
            requestDto.setItems(new ArrayList<>());
            if (map.containsKey(r)) {
                requestDto.setItems(ItemMapper.itemDtoList(map.get(r)));
            }
            requestDtos.add(requestDto);
        }
        return requestDtos;
    }
}
