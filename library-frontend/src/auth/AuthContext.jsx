import { createContext, useContext, useState } from "react";
import api from "../api/api";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(
        !!localStorage.getItem("access_token")
    );

    const login = async (username, password) => {
        const response = await api.post("/auth/login", {
            username,
            password,
        });

        const access_token = response.data.access_token;

        // store token
        localStorage.setItem("access_token", access_token);
        setIsAuthenticated(true);
    };

    const logout = () => {
        localStorage.removeItem("access_token");
        setIsAuthenticated(false);
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
