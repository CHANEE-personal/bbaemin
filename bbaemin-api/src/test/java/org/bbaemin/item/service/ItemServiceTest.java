package org.bbaemin.item.service;

import org.bbaemin.item.controller.request.CreateItemRequest;
import org.bbaemin.item.controller.request.UpdateItemRequest;
import org.bbaemin.item.controller.response.ItemImageResponse;
import org.bbaemin.item.controller.response.ItemResponse;
import org.bbaemin.item.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private static List<ItemResponse> itemList = new ArrayList<>();
    private static Long itemId = 0L;

    void createItem() {
        itemList.add(
                ItemResponse.builder()
                        .itemId(++itemId)
                        .category("과일")
                        .store("B마트 인천점")
                        .name("청동사과")
                        .description("맛있고 싱싱한 청동사과")
                        .price(2000)
                        .quantity(999)
                        .itemImageResponse(
                                Arrays.asList(
                                        ItemImageResponse.builder()
                                                .url("https://image.thumbnail.com")
                                                .type("thumb-nail")
                                                .build(),
                                        ItemImageResponse.builder()
                                                .url("https://image.detail.com")
                                                .type("detail")
                                                .build()
                                )
                        )
                        .build());

        itemList.add(
                ItemResponse.builder()
                        .itemId(++itemId)
                        .category("유제품")
                        .store("B마트 인천점")
                        .name("서울우유 저지방우유 1000ml")
                        .description("서울 우유 저지방우유 1000ml")
                        .price(5000)
                        .quantity(999)
                        .itemImageResponse(
                                Arrays.asList(
                                        ItemImageResponse.builder()
                                                .url("https://image.thumbnail.com")
                                                .type("thumbnail")
                                                .build(),
                                        ItemImageResponse.builder()
                                                .url("https://image.detail.com")
                                                .type("detail")
                                                .build()
                                )
                        )
                        .build()
        );
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createItem();
    }

    @Test
    @DisplayName("아이템 리스트 조회")
    void 아이템리스트조회() {
        // when
        when(itemRepository.listItem()).thenReturn(itemList);
        List<ItemResponse> itemList = itemService.listItem();

        // then
        assertThat(itemList.get(0).getCategory()).isEqualTo("과일");
        assertThat(itemList.get(0).getName()).isEqualTo("청동사과");
        assertThat(itemList.get(0).getPrice()).isEqualTo(2000);

        assertThat(itemList.get(1).getCategory()).isEqualTo("유제품");
        assertThat(itemList.get(1).getName()).isEqualTo("서울우유 저지방우유 1000ml");
        assertThat(itemList.get(1).getPrice()).isEqualTo(5000);

        // verify
        verify(itemRepository, times(1)).listItem();
        verify(itemRepository, atLeastOnce()).listItem();
        verifyNoMoreInteractions(itemRepository);

        InOrder inOrder = inOrder(itemRepository);
        inOrder.verify(itemRepository).listItem();
    }

    @Test
    @DisplayName("아이템 상세 조회")
    void 아이템상세조회() {
        // when
        when(itemRepository.getItem(1L)).thenReturn(itemList.get(0));
        ItemResponse getItem = itemService.getItem(1L);

        // then
        assertThat(getItem.getCategory()).isEqualTo("과일");
        assertThat(getItem.getName()).isEqualTo("청동사과");
        assertThat(getItem.getPrice()).isEqualTo(2000);

        // verify
        verify(itemRepository, times(1)).getItem(1L);
        verify(itemRepository, atLeastOnce()).getItem(1L);
        verifyNoMoreInteractions(itemRepository);

        InOrder inOrder = inOrder(itemRepository);
        inOrder.verify(itemRepository).getItem(1L);
    }

    @Test
    @DisplayName("아이템 등록")
    void 아이템등록() {
        CreateItemRequest createItemRequest = CreateItemRequest.builder()
                .categoryId(1L)
                .storeId(1L)
                .name("달달한 초코칩")
                .description("순도 100% 초콜릿 초코칩")
                .price(3000)
                .quantity(999)
                .itemImageRequest(
                        Arrays.asList(
                                CreateItemRequest.ItemImageRequest.builder()
                                        .itemId(3L)
                                        .url("https://image.thumbnail.com")
                                        .type("thumbnail")
                                        .build(),
                                CreateItemRequest.ItemImageRequest.builder()
                                        .itemId(3L)
                                        .url("https://image.detail.com")
                                        .type("detail")
                                        .build()
                        ))
                .build();

        ItemResponse createResponse = ItemResponse.builder()
                .itemId(itemId)
                .category("과자")
                .store("B마트 계양점")
                .name(createItemRequest.getName())
                .description(createItemRequest.getDescription())
                .price(createItemRequest.getPrice())
                .quantity(createItemRequest.getQuantity())
                .itemImageResponse(
                        Arrays.asList(
                                ItemImageResponse.builder()
                                        .url("https://image.thumbnail.com")
                                        .type("thumbnail")
                                        .build(),
                                ItemImageResponse.builder()
                                        .url("https://image.detail.com")
                                        .type("detail")
                                        .build()
                        )
                )
                .build();

        // when
        when(itemRepository.createItem(createItemRequest)).thenReturn(createResponse);
        ItemResponse getItem = itemService.createItem(createItemRequest);

        // then
        assertThat(getItem.getName()).isEqualTo("달달한 초코칩");
        assertThat(getItem.getDescription()).isEqualTo("순도 100% 초콜릿 초코칩");
        assertThat(getItem.getPrice()).isEqualTo(3000);

        // verify
        verify(itemRepository, times(1)).createItem(createItemRequest);
        verify(itemRepository, atLeastOnce()).createItem(createItemRequest);
        verifyNoMoreInteractions(itemRepository);

        InOrder inOrder = inOrder(itemRepository);
        inOrder.verify(itemRepository).createItem(createItemRequest);
    }

    @Test
    @DisplayName("아이템 수정")
    void 아이템수정() {
        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .categoryId(999L)
                .storeId(999L)
                .name("닭고기 100g")
                .description("닭고기 100g")
                .price(5000)
                .quantity(999)
                .build();

        ItemResponse updateResponse = ItemResponse.builder()
                .itemId(itemId)
                .category("과자")
                .store("B마트 계양점")
                .name(updateItemRequest.getName())
                .description(updateItemRequest.getDescription())
                .price(updateItemRequest.getPrice())
                .quantity(updateItemRequest.getQuantity())
                .itemImageResponse(
                        Arrays.asList(
                                ItemImageResponse.builder()
                                        .url("https://image.thumbnail.com")
                                        .type("thumbnail")
                                        .build(),
                                ItemImageResponse.builder()
                                        .url("https://image.detail.com")
                                        .type("detail")
                                        .build()
                        )
                )
                .build();

        // when
        when(itemRepository.updateItem(2L, updateItemRequest)).thenReturn(updateResponse);
        ItemResponse updateItem = itemService.updateItem(2L, updateItemRequest);

        // then
        assertThat(updateItem.getName()).isEqualTo("닭고기 100g");
        assertThat(updateItem.getDescription()).isEqualTo("닭고기 100g");

        // verify
        verify(itemRepository, times(1)).updateItem(2L, updateItemRequest);
        verify(itemRepository, atLeastOnce()).updateItem(2L, updateItemRequest);
        verifyNoMoreInteractions(itemRepository);

        InOrder inOrder = inOrder(itemRepository);
        inOrder.verify(itemRepository).updateItem(2L, updateItemRequest);
    }

    @Test
    @DisplayName("아이템 삭제")
    void 아이템삭제() {
        // when
        when(itemRepository.deleteItem(2L)).thenReturn(2L);
        Long deleteItemId = itemService.deleteItem(2L);

        // then
        assertThat(deleteItemId).isEqualTo(2L);

        // verify
        verify(itemRepository, times(1)).deleteItem(2L);
        verify(itemRepository, atLeastOnce()).deleteItem(2L);
        verifyNoMoreInteractions(itemRepository);

        InOrder inOrder = inOrder(itemRepository);
        inOrder.verify(itemRepository).deleteItem(2L);
    }
}
