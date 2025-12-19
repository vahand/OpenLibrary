import { useState } from "react";
import api from "../api/api";

export default function BookForm({ onAdded }) {
    const [title, setTitle] = useState("");
    const [author, setAuthor] = useState("");

    const submit = async (e) => {
        e.preventDefault();
        await api.post("/api/books", { title, author });
        setTitle("");
        setAuthor("");
        onAdded();
    };

    return (
        <form onSubmit={submit}>
            <input placeholder="Title" value={title} onChange={e => setTitle(e.target.value)} />
            <input placeholder="Author" value={author} onChange={e => setAuthor(e.target.value)} />
            <button>Add Book</button>
        </form>
    );
}
