import axios, { AxiosError, AxiosResponse } from 'axios';
import { Book, CreateBookRequest, UpdateBookRequest, ApiError } from '../types/Book';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api/books';

// Create axios instance with default config
const apiClient = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
    timeout: 10000, // 10 seconds
});

// Add response interceptor for error handling
apiClient.interceptors.response.use(
    (response) => response,
    (error: AxiosError) => {
        const apiError: ApiError = {
            message: error.response?.data as string || error.message || 'An unexpected error occurred',
            status: error.response?.status,
        };
        return Promise.reject(apiError);
    }
);

class BookService {
    /* Get all books */

    async getAllBooks(): Promise<Book[]> {
        const response: AxiosResponse<Book[]> = await  apiClient.get('');
        return response.data;
    }

    /*Get book by ID*/
    async getBookById(id: number): Promise<Book> {
        const response: AxiosResponse<Book> = await apiClient.get(`/${id}`);
        return response.data;
    }

    /*Get book by ISBN*/
    async getBookByIsbn(isbn: string): Promise<Book> {
        const response: AxiosResponse<Book> = await apiClient.get(`/isbn/${isbn}`);
        return response.data;
    }

    /*Get books by category*/
    async getBooksByCategory(category: string): Promise<Book[]> {
        const response: AxiosResponse<Book[]> = await apiClient.get(`/category/${category}`);
        return response.data;
    }

    /**Search books by title*/
    async searchByTitle(query: string): Promise<Book[]> {
        const response: AxiosResponse<Book[]> = await apiClient.get('/search/title', {
            params: { query },
        });
        return response.data;
    }

    /*Search books by author*/
    async searchByAuthor(query: string): Promise<Book[]> {
        const response: AxiosResponse<Book[]> = await apiClient.get('/search/author', {
            params: { query },
        });
        return response.data;
    }

    /*Create a new book*/
    async createBook(book: CreateBookRequest): Promise<Book> {
        const response: AxiosResponse<Book> = await apiClient.post('', book);
        return response.data;
    }

    /*Update an existing book*/
    async updateBook(id: number, book: UpdateBookRequest): Promise<Book> {
        const response: AxiosResponse<Book> = await apiClient.put(`/${id}`, book);
        return response.data;
    }

    /*Delete a book*/
    async deleteBook(id: number): Promise<void> {
        await apiClient.delete(`/${id}`);
    }
}
const bookService = new BookService();

export default bookService;