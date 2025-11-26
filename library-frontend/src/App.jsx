import { useEffect, useState } from 'react';

function App() {
    const [books, setBooks] = useState([]);
    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');

    useEffect(()=> {
        fetch('http://localhost:8080/api/books')
            .then(r=>r.json())
            .then(setBooks)
            .catch(console.error);
    }, []);

    function addBook(e){
        e.preventDefault();
        fetch('http://localhost:8080/api/books', {
            method: 'POST',
            headers: {'Content-Type':'application/json'},
            body: JSON.stringify({title, author})
        })
            .then(r=>r.json())
            .then(newBook => setBooks(b=>[...b, newBook]));
    }

    return (
        <div style={{padding:20}}>
            <h1>OpenLibrary tool</h1>
            <form onSubmit={addBook}>
                <input placeholder="Title" value={title} onChange={e=>setTitle(e.target.value)} />
                <input placeholder="Author" value={author} onChange={e=>setAuthor(e.target.value)} />
                <button>Add</button>
            </form>

            <ul>
                {books.map(b => <li key={b.id}>{b.title} — {b.author}</li>)}
            </ul>
        </div>
    );
}

export default App;
