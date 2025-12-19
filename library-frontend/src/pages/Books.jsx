import { useEffect, useState } from "react";
import api from "../api/api";
import { useAuth } from "../auth/AuthContext.jsx";
import BookForm from "../components/BookForm";

export default function Books() {
    const [books, setBooks] = useState([]);
    const { logout } = useAuth();

    const loadBooks = async () => {
        const res = await api.get("/api/books");
        setBooks(res.data);
    };

    const deleteBook = async (id) => {
        await api.delete(`/api/books/${id}`);
        loadBooks();
    };

    useEffect(() => {
        loadBooks();
    }, []);

    return (
        <div className="container">
            <h2>Book Reserve</h2>
            <button onClick={logout}>Logout</button>

            <BookForm onAdded={loadBooks} />

            <ul>
                {books.map(book => (
                    <li key={book.id}>
                        {book.title} — {book.author}
                        <button onClick={() => deleteBook(book.id)}>Delete</button>
                    </li>
                ))}
            </ul>
        </div>
    );
}
