import React, { useState, FormEvent, ChangeEvent } from 'react';
import { SearchType } from '../types/Book';
import './SearchBar.css';

interface SearchBarProps {
    onSearch: (searchTerm: string, searchType: SearchType) => void;
    onReset: () => void;
}

const SearchBar: React.FC<SearchBarProps> = ({ onSearch, onReset }) => {
    const [searchTerm, setSearchTerm] = useState<string>('');
    const [searchType, setSearchType] = useState<SearchType>('title');

    const handleSubmit = (e: FormEvent<HTMLFormElement>): void => {
        e.preventDefault();
        onSearch(searchTerm, searchType);
    };

    const handleReset = (): void => {
        setSearchTerm('');
        onReset();
    };

    const handleSearchTypeChange = (e: ChangeEvent<HTMLSelectElement>): void => {
        setSearchType(e.target.value as SearchType);
    };

    const handleSearchTermChange = (e: ChangeEvent<HTMLInputElement>): void => {
        setSearchTerm(e.target.value);
    };

    return (
        <form onSubmit={handleSubmit} className="search-form">
            <select
                value={searchType}
                onChange={handleSearchTypeChange}
                className="search-select"
                aria-label="Search type"
            >
                <option value="title">Title</option>
                <option value="author">Author</option>
            </select>
            <input
                type="text"
                placeholder={`Search by ${searchType}...`}
                value={searchTerm}
                onChange={handleSearchTermChange}
                className="search-input"
                aria-label="Search term"
            />
            <button type="submit" className="search-button">
                Search
            </button>
            <button
                type="button"
                onClick={handleReset}
                className="reset-button"
            >
                Reset
            </button>
        </form>
    );
};

export default SearchBar;