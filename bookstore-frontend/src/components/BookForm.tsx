import React, { useState, FormEvent, ChangeEvent } from 'react';
import { CreateBookRequest } from '../types/Book';
import './BookForm.css';

interface BookFormProps {
    onSubmit: (book: CreateBookRequest) => Promise<void>;
    onCancel: () => void;
}

interface FormErrors {
    title?: string;
    author?: string;
    isbn?: string;
    price?: string;
    description?: string;
    stockQuantity?: string;
    publishedDate?: string;
    category?: string;
}

const BookForm: React.FC<BookFormProps> = ({ onSubmit, onCancel }) => {
    const [formData, setFormData] = useState<CreateBookRequest>({
        title: '',
        author: '',
        isbn: '',
        price: 0,
        description: '',
        stockQuantity: 0,
        publishedDate: '',
        category: '',
        // TODO, remove this after we have userId
        userId: 1
    });

    const [errors, setErrors] = useState<FormErrors>({});
    const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

    const handleChange = (
        e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
    ): void => {
        const { name, value } = e.target;

        setFormData((prev) => ({
            ...prev,
            [name]: name === 'price' || name === 'stockQuantity'
                ? parseFloat(value) || 0
                : value,
        }));

        // Clear error for this field when user starts typing
        if (errors[name as keyof FormErrors]) {
            setErrors((prev) => ({
                ...prev,
                [name]: undefined,
            }));
        }
    };

    const validateForm = (): boolean => {
        const newErrors: FormErrors = {};

        // Title validation
        if (!formData.title.trim()) {
            newErrors.title = 'Title is required';
        } else if (formData.title.length > 255) {
            newErrors.title = 'Title must not exceed 255 characters';
        }

        // Author validation
        if (!formData.author.trim()) {
            newErrors.author = 'Author is required';
        } else if (formData.author.length > 255) {
            newErrors.author = 'Author must not exceed 255 characters';
        }

        // ISBN validation
        if (!formData.isbn.trim()) {
            newErrors.isbn = 'ISBN is required';
        } else if (!/^\d{13}$/.test(formData.isbn)) {
            newErrors.isbn = 'ISBN must be exactly 13 digits';
        }

        // Price validation
        if (formData.price <= 0) {
            newErrors.price = 'Price must be greater than 0';
        }

        // Stock quantity validation
        if (formData.stockQuantity < 0) {
            newErrors.stockQuantity = 'Stock quantity cannot be negative';
        }

        // Published date validation
        if (!formData.publishedDate) {
            newErrors.publishedDate = 'Published date is required';
        } else {
            const publishedDate = new Date(formData.publishedDate);
            const today = new Date();
            if (publishedDate > today) {
                newErrors.publishedDate = 'Published date must be in the past';
            }
        }

        // Category validation
        if (!formData.category.trim()) {
            newErrors.category = 'Category is required';
        }

        // Description validation (optional but has max length)
        if (formData.description && formData.description.length > 5000) {
            newErrors.description = 'Description must not exceed 5000 characters';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e: FormEvent<HTMLFormElement>): Promise<void> => {
        e.preventDefault();

        if (!validateForm()) {
            return;
        }

        setIsSubmitting(true);
        try {
            await onSubmit(formData);
        } catch (error) {
            console.error('Error submitting form:', error);
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="modal-overlay" onClick={onCancel}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>Add New Book</h2>
                    <button className="close-button" onClick={onCancel} aria-label="Close">
                        ×
                    </button>
                </div>

                <form onSubmit={handleSubmit} className="book-form">
                    <div className="form-row">
                        <div className="form-group">
                            <label htmlFor="title">
                                Title <span className="required">*</span>
                            </label>
                            <input
                                type="text"
                                id="title"
                                name="title"
                                value={formData.title}
                                onChange={handleChange}
                                className={errors.title ? 'error' : ''}
                                placeholder="Enter book title"
                            />
                            {errors.title && <span className="error-message">{errors.title}</span>}
                        </div>

                        <div className="form-group">
                            <label htmlFor="author">
                                Author <span className="required">*</span>
                            </label>
                            <input
                                type="text"
                                id="author"
                                name="author"
                                value={formData.author}
                                onChange={handleChange}
                                className={errors.author ? 'error' : ''}
                                placeholder="Enter author name"
                            />
                            {errors.author && <span className="error-message">{errors.author}</span>}
                        </div>
                    </div>

                    <div className="form-row">
                        <div className="form-group">
                            <label htmlFor="isbn">
                                ISBN <span className="required">*</span>
                            </label>
                            <input
                                type="text"
                                id="isbn"
                                name="isbn"
                                value={formData.isbn}
                                onChange={handleChange}
                                className={errors.isbn ? 'error' : ''}
                                placeholder="13 digits"
                                maxLength={13}
                            />
                            {errors.isbn && <span className="error-message">{errors.isbn}</span>}
                        </div>

                        <div className="form-group">
                            <label htmlFor="category">
                                Category <span className="required">*</span>
                            </label>
                            <select
                                id="category"
                                name="category"
                                value={formData.category}
                                onChange={handleChange}
                                className={errors.category ? 'error' : ''}
                            >
                                <option value="">Select category</option>
                                <option value="Fiction">Fiction</option>
                                <option value="Non-Fiction">Non-Fiction</option>
                                <option value="Science Fiction">Science Fiction</option>
                                <option value="Fantasy">Fantasy</option>
                                <option value="Mystery">Mystery</option>
                                <option value="Romance">Romance</option>
                                <option value="Technology">Technology</option>
                                <option value="Biography">Biography</option>
                                <option value="History">History</option>
                                <option value="Self-Help">Self-Help</option>
                            </select>
                            {errors.category && <span className="error-message">{errors.category}</span>}
                        </div>
                    </div>

                    <div className="form-row">
                        <div className="form-group">
                            <label htmlFor="price">
                                Price ($) <span className="required">*</span>
                            </label>
                            <input
                                type="number"
                                id="price"
                                name="price"
                                value={formData.price || ''}
                                onChange={handleChange}
                                className={errors.price ? 'error' : ''}
                                placeholder="0.00"
                                step="0.01"
                                min="0"
                            />
                            {errors.price && <span className="error-message">{errors.price}</span>}
                        </div>

                        <div className="form-group">
                            <label htmlFor="stockQuantity">
                                Stock Quantity <span className="required">*</span>
                            </label>
                            <input
                                type="number"
                                id="stockQuantity"
                                name="stockQuantity"
                                value={formData.stockQuantity || ''}
                                onChange={handleChange}
                                className={errors.stockQuantity ? 'error' : ''}
                                placeholder="0"
                                min="0"
                            />
                            {errors.stockQuantity && (
                                <span className="error-message">{errors.stockQuantity}</span>
                            )}
                        </div>

                        <div className="form-group">
                            <label htmlFor="publishedDate">
                                Published Date <span className="required">*</span>
                            </label>
                            <input
                                type="date"
                                id="publishedDate"
                                name="publishedDate"
                                value={formData.publishedDate}
                                onChange={handleChange}
                                className={errors.publishedDate ? 'error' : ''}
                                max={new Date().toISOString().split('T')[0]}
                            />
                            {errors.publishedDate && (
                                <span className="error-message">{errors.publishedDate}</span>
                            )}
                        </div>
                    </div>

                    <div className="form-group">
                        <label htmlFor="description">Description</label>
                        <textarea
                            id="description"
                            name="description"
                            value={formData.description}
                            onChange={handleChange}
                            className={errors.description ? 'error' : ''}
                            placeholder="Enter book description (optional)"
                            rows={4}
                        />
                        {errors.description && (
                            <span className="error-message">{errors.description}</span>
                        )}
                    </div>

                    <div className="form-actions">
                        <button
                            type="button"
                            onClick={onCancel}
                            className="cancel-button"
                            disabled={isSubmitting}
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            className="submit-button"
                            disabled={isSubmitting}
                        >
                            {isSubmitting ? 'Creating...' : 'Create Book'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default BookForm;