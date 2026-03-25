export interface Book {
    id: number;
    title: string;
    author: string;
    isbn: string;
    price: number;
    description: string;
    stockQuantity: number;
    publishedDate: string;
    category: string;
    createdAt: string;
    updatedAt: string;
}

export interface CreateBookRequest {
    title: string;
    author: string;
    isbn: string;
    price: number;
    description: string;
    stockQuantity: number;
    publishedDate: string;
    category: string;
}

export interface UpdateBookRequest {
    title: string;
    author: string;
    isbn: string;
    price: number;
    description: string;
    stockQuantity: number;
    publishedDate: string;
    category: string;
}

export type SearchType = 'title' | 'author';

export interface ApiError {
    message: string;
    status?: number;
}