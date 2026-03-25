import React from 'react';
import BookList from './components/BookList';
import './App.css';

const App: React.FC = () => {
  return (
      <div className="App">
        <BookList />
      </div>
  );
};

export default App;