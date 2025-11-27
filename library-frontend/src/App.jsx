import { useEffect, useState, useContext } from 'react';
import { AuthContext } from './auth/AuthProvider.jsx';

function App() {
    const { keycloak } = useContext(AuthContext);
    const [books, setBooks] = useState([]);
    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');

    useEffect(()=> {
        fetch('http://localhost:8080/api/books', {
            headers: {
                'Content-Type': 'application/json',
                Authorization: "Bearer " + keycloak.token
            }
        })
            .then(r=>r.json())
            .then(setBooks)
            .catch(console.error);
    }, []);

    function addBook(e){
        e.preventDefault();
        fetch('http://localhost:8080/api/books', {
            method: 'POST',
            headers: {'Content-Type':'application/json', Authorization: "Bearer " + keycloak.token},
            body: JSON.stringify({title, author})
        })
            .then(r=>r.json())
            .then(newBook => setBooks(b=>[...b, newBook]));
    }

    function getStatus(){
        fetch('http://localhost:8080/public/status', {
            headers: {'Content-Type':'application/json'}
        })
            .then(r=>r.json())
            .then(status => {
                console.log("Status:", status);
            })
            .catch(console.error);
    }

    return (
        <div style={{padding:20}}>
            <h1>OpenLibrary tool</h1>
            <button onClick={getStatus}>Get Status</button>

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
