package com.example.BookStore.mappers;

import com.example.BookStore.dtos.BookDTO;
import com.example.BookStore.dtos.CreateBookRequest;
import com.example.BookStore.dtos.UpdateBookRequest;
import com.example.BookStore.entities.Book;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {
    public BookDTO toDto(Book book) {
        if (book == null) {
            return null;
        }

        BookDTO.BookDTOBuilder bookBuilder = BookDTO.builder();
        bookBuilder
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .price(book.getPrice())
                .description(book.getDescription())
                .stockQuantity(book.getStockQuantity())
                .publishedDate(book.getPublishedDate())
                .category(book.getCategory())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt());

        return bookBuilder.build();
    }

    public Book toEntity(BookDTO bookDTO) {
        if (bookDTO == null) {
            return null;
        }

        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        book.setPrice(bookDTO.getPrice());
        book.setDescription(bookDTO.getDescription());
        book.setStockQuantity(bookDTO.getStockQuantity());
        book.setPublishedDate(bookDTO.getPublishedDate());
        book.setCategory(bookDTO.getCategory());

        return book;
    }

    /**
     * Convert CreateBookRequest to Entity
     */
    public Book toEntity(CreateBookRequest request) {
        if (request == null) {
            return null;
        }

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setDescription(request.getDescription());
        book.setStockQuantity(request.getStockQuantity());
        book.setPublishedDate(request.getPublishedDate());
        book.setCategory(request.getCategory());

        return book;
    }

    public void updateEntityFromRequest(UpdateBookRequest request, Book book) {
        if (request == null || book == null) {
            return;
        }

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setDescription(request.getDescription());
        book.setStockQuantity(request.getStockQuantity());
        book.setPublishedDate(request.getPublishedDate());
        book.setCategory(request.getCategory());
    }

    public List<BookDTO> toDtoList(List<Book> books) {
        if (books == null) {
            return null;
        }

        return books.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Book> toEntityList(List<BookDTO> bookDTOs) {
        if (bookDTOs == null) {
            return null;
        }

        return bookDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
