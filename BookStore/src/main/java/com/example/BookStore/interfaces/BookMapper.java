package com.example.BookStore.interfaces;

import com.example.BookStore.dtos.BookDTO;
import com.example.BookStore.dtos.CreateBookRequest;
import com.example.BookStore.dtos.UpdateBookRequest;
import com.example.BookStore.entities.Book;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDTO toDto(Book book);

    Book toEntity(BookDTO bookDTO);

    Book toEntity(CreateBookRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UpdateBookRequest request, @MappingTarget Book book);

    List<BookDTO> toDtoList(List<Book> books);

    List<Book> toEntityList(List<BookDTO> bookDTOs);
}
