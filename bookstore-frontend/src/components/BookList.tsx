import React, { useState, useEffect, useCallback } from 'react';
import bookService from '../services/bookService';
import { Book, SearchType, ApiError, CreateBookRequest } from '../types/Book';
import BookCard from './BookCard';
import SearchBar from './SearchBar';
import BookForm from './BookForm';
import './BookList.css';

const BookList: React.FC = () => {
    const [books, setBooks] = useState<Book[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [showCreateForm, setShowCreateForm] = useState<boolean>(false);
    const [successMessage, setSuccessMessage] = useState<string | null>(null);

    const fetchBooks = useCallback(async (): Promise<void> => {
        try {
            setLoading(true);
            setError(null);
            const data = await bookService.getAllBooks();
            setBooks(data);
        } catch (err) {
            const apiError = err as ApiError;
            setError(apiError.message || 'Failed to fetch books. Please try again.');
            console.error('Error fetching books:', err);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchBooks();
    }, [fetchBooks]);

    // Auto-hide success message after 3 seconds
    useEffect(() => {
        if (successMessage) {
            const timer = setTimeout(() => {
                setSuccessMessage(null);
            }, 3000);
            return () => clearTimeout(timer);
        }
    }, [successMessage]);

    const handleSearch = async (searchTerm: string, searchType: SearchType): Promise<void> => {
        if (!searchTerm.trim()) {
            fetchBooks();
            return;
        }

        try {
            setLoading(true);
            setError(null);

            let data: Book[];
            if (searchType === 'title') {
                data = await bookService.searchByTitle(searchTerm);
            } else {
                data = await bookService.searchByAuthor(searchTerm);
            }

            setBooks(data);
        } catch (err) {
            const apiError = err as ApiError;
            setError(apiError.message || 'Search failed. Please try again.');
            console.error('Error searching books:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id: number): Promise<void> => {
        const book = books.find(b => b.id === id);
        const bookTitle = book ? book.title : 'this book';

        if (window.confirm(`Are you sure you want to delete "${bookTitle}"?`)) {
            try {
                await bookService.deleteBook(id);
                setBooks(prevBooks => prevBooks.filter(b => b.id !== id));
                setSuccessMessage('Book deleted successfully!');
            } catch (err) {
                const apiError = err as ApiError;
                setError(apiError.message || 'Failed to delete book.');
                console.error('Error deleting book:', err);
                fetchBooks();
            }
        }
    };

    const handleCreateBook = async (bookData: CreateBookRequest): Promise<void> => {
        try {
            const newBook = await bookService.createBook(bookData);
            setBooks(prevBooks => [newBook, ...prevBooks]);
            setShowCreateForm(false);
            setSuccessMessage('Book created successfully!');
        } catch (err) {
            const apiError = err as ApiError;
            setError(apiError.message || 'Failed to create book.');
            console.error('Error creating book:', err);
            throw err; // Re-throw to let the form handle it
        }
    };

    if (loading) {
        return (
            <div className="book-list-container">
                <div className="loading">
                    <div className="spinner"></div>
                    <p>Loading books...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="book-list-container">
            <div className="header-section">
                <h1>📚 Bookstore</h1>
                <button
                    className="create-book-button"
                    onClick={() => setShowCreateForm(true)}
                >
                    + Add New Book
                </button>
            </div>

            <SearchBar onSearch={handleSearch} onReset={fetchBooks} />

            {successMessage && (
                <div className="success-message">
                    <p>✓ {successMessage}</p>
                </div>
            )}

            {error && (
                <div className="error-message">
                    <p>{error}</p>
                    <button onClick={fetchBooks} className="retry-button">
                        Try Again
                    </button>
                </div>
            )}

            <div className="books-grid">
                {books.length === 0 && !error ? (
                    <div className="no-books">
                        <p>No books found.</p>
                        <p className="suggestion">Try adjusting your search criteria or adding new books.</p>
                    </div>
                ) : (
                    books.map((book) => (
                        <BookCard key={book.id} book={book} onDelete={handleDelete} />
                    ))
                )}
            </div>

            {showCreateForm && (
                <BookForm
                    onSubmit={handleCreateBook}
                    onCancel={() => setShowCreateForm(false)}
                />
            )}
        </div>
    );
};

export default BookList;