package com.example.BookStore.services;

import com.example.BookStore.dtos.BookDTO;
import com.example.BookStore.dtos.CreateBookRequest;
import com.example.BookStore.dtos.UpdateBookRequest;
import com.example.BookStore.entities.Book;
import com.example.BookStore.mappers.BookMapper;
import com.example.BookStore.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldReturnAllBooks() {
        List<Book> books = List.of(new Book());
        List<BookDTO> dtos = List.of(BookDTO.builder().build());

        when(bookRepository.findAll()).thenReturn(books);
        when(bookMapper.toDtoList(books)).thenReturn(dtos);

        List<BookDTO> result = bookService.getAllBooks();

        assertEquals(1, result.size());
        verify(bookRepository).findAll();
    }

    @Test
    void shouldThrowWhenBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            bookService.getBookById(1L);
        });
    }

    @Test
    void shouldThrowWhenBookNotFoundByIsbn() {
        when(bookRepository.findByIsbn("2321231231231")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            bookService.getBookByIsbn("2321231231231");
        });
    }

    @Test
    void shouldReturnBookByIsbn() {
        Book book = new Book();
        BookDTO bookDTO = BookDTO.builder().build();

        when(bookRepository.findByIsbn("2321231231236")).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDTO);

        BookDTO result = bookService.getBookByIsbn("2321231231236");

        assertEquals(bookDTO, result);
        verify(bookRepository).findByIsbn("2321231231236");
    }

    @Test
    void shouldReturnAllBooksByCategory() {
        List<Book> books = List.of(new Book());
        List<BookDTO> dtos = List.of(BookDTO.builder().build());

        when(bookRepository.findByCategory("Romance")).thenReturn(books);
        when(bookMapper.toDtoList(books)).thenReturn(dtos);

        List<BookDTO> result = bookService.getBookByCategory("Romance");

        assertEquals(1, result.size());
        verify(bookRepository).findByCategory("Romance");
    }

    @Test
    void shouldSearchBooksByTitle() {
        List<Book> books = List.of(new Book());
        List<BookDTO> dtos = List.of(BookDTO.builder().build());

        when(bookRepository.findByTitleContainingIgnoreCase("Harry")).thenReturn(books);
        when(bookMapper.toDtoList(books)).thenReturn(dtos);

        List<BookDTO> result = bookService.searchBooksByTitle("Harry");

        assertEquals(1, result.size());
        verify(bookRepository).findByTitleContainingIgnoreCase("Harry");
    }

    @Test
    void shouldSearchBooksByAuthor() {
        List<Book> books = List.of(new Book());
        List<BookDTO> dtos = List.of(BookDTO.builder().build());

        when(bookRepository.findByAuthorContainingIgnoreCase("Charlie Chaplin")).thenReturn(books);
        when(bookMapper.toDtoList(books)).thenReturn(dtos);

        List<BookDTO> result = bookService.searchBooksByAuthor("Charlie Chaplin");

        assertEquals(1, result.size());
        verify(bookRepository).findByAuthorContainingIgnoreCase("Charlie Chaplin");
    }

    @Test
    void shouldCreateBook() {
        CreateBookRequest createBookReq = new CreateBookRequest();
        Book book = new Book();
        BookDTO bookDTO = BookDTO.builder().build();

        when(bookMapper.toEntity(createBookReq)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDTO);

        BookDTO result = bookService.createBook(createBookReq);

        assertEquals(bookDTO, result);
        verify(bookRepository).save(book);
    }

    @Test
    void shouldUpdateBook() {
        Long id = 1L;
        UpdateBookRequest updateBookReq = new UpdateBookRequest();
        Book book = new Book();
        BookDTO bookDTO = BookDTO.builder().build();

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDTO);

        BookDTO result = bookService.updateBook(id, updateBookReq);

        assertEquals(bookDTO, result);
        verify(bookMapper).updateEntityFromRequest(updateBookReq, book);
        verify(bookRepository).save(book);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentBook() {
        Long id = 1L;
        UpdateBookRequest updateBookReq = new UpdateBookRequest();

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookService.updateBook(id, updateBookReq));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void shouldDeleteBook() {
        Long id = 1L;
        Book book = new Book();

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        bookService.deleteBook(id);

        verify(bookRepository).delete(book);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentBook() {
        Long id = 1L;

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookService.deleteBook(id));
        verify(bookRepository, never()).delete(any());
    }
}

