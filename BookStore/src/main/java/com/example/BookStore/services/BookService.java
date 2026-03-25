package com.example.BookStore.services;

import com.example.BookStore.dtos.BookDTO;
import com.example.BookStore.dtos.CreateBookRequest;
import com.example.BookStore.dtos.UpdateBookRequest;
import com.example.BookStore.entities.Book;
import com.example.BookStore.entities.User;
import com.example.BookStore.mappers.BookMapper;
import com.example.BookStore.repositories.BookRepository;
import com.example.BookStore.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;

    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return bookMapper.toDtoList(books);
    }

    public BookDTO getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Book not found with id : " + isbn));
        return bookMapper.toDto(book);
    }

    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id : " + id));
        return bookMapper.toDto(book);
    }

    public List<BookDTO> getBookByCategory(String category) {
        List<Book> books = bookRepository.findByCategory(category);
        return bookMapper.toDtoList(books);
    }

    public List<BookDTO> searchBooksByTitle(String title) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
        return bookMapper.toDtoList(books);
    }

    public List<BookDTO> searchBooksByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthorContainingIgnoreCase(author);
        return bookMapper.toDtoList(books);
    }

    @Transactional
    public BookDTO createBook(CreateBookRequest request) {
        // TODO wire the user id to the books since books can have multiple users
        User foundUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id : " + request.getUserId()));

        Book book = bookMapper.toEntity(request);
        book.setUser(foundUser);
        Book savedBook = bookRepository.save(book);

        return bookMapper.toDto(savedBook);
    }

    @Transactional
    public BookDTO updateBook(Long id, UpdateBookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        bookMapper.updateEntityFromRequest(request, book);

        Book updatedBook = bookRepository.save(book);

        return bookMapper.toDto(updatedBook);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        bookRepository.delete(book);
    }
}
