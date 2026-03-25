import React from 'react';
import { Book } from '../types/Book';
import './BookCard.css';

interface BookCardProps {
    book: Book;
    onDelete: (id: number) => void;
}

const BookCard: React.FC<BookCardProps> = ({ book, onDelete }) => {
    const formatDate = (dateString: string): string => {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
        });
    };

    const formatPrice = (price: number): string => {
        return price.toFixed(2);
    };

    const handleDelete = (): void => {
        onDelete(book.id);
    };

    return (
        <div className="book-card">
            <div className="book-header">
                <h3 className="book-title">{book.title}</h3>
                <span className="category-badge">{book.category}</span>
            </div>

            <p className="author">by {book.author}</p>

            <p className="description">{book.description}</p>

            <div className="book-details">
                <p>
                    <strong>ISBN:</strong> {book.isbn}
                </p>
                <p>
                    <strong>Published:</strong> {formatDate(book.publishedDate)}
                </p>
                <p>
                    <strong>Stock:</strong> {book.stockQuantity} units
                </p>
            </div>

            <div className="book-footer">
                <span className="price">${formatPrice(book.price)}</span>
                <button
                    onClick={handleDelete}
                    className="delete-button"
                    aria-label={`Delete ${book.title}`}
                >
                    Delete
                </button>
            </div>
        </div>
    );
};

export default BookCard;