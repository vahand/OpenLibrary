import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext.jsx";

export default function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await login(username, password);
            navigate("/");
        } catch {
            setError("Invalid credentials");
        }
    };

    return (
        <div className="container">
            <h2>Library Login</h2>
            <form onSubmit={handleSubmit}>
                <input placeholder="Username" onChange={e => setUsername(e.target.value)} />
                <input type="password" placeholder="Password" onChange={e => setPassword(e.target.value)} />
                <button>Login</button>
                {error && <p className="error">{error}</p>}
            </form>
        </div>
    );
}
