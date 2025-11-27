import { createContext, useState, useEffect } from "react";
import keycloak from "./../keycloak.js";

export const AuthContext = createContext();

export default function AuthProvider({ children }) {
    const [authenticated, setAuthenticated] = useState(false);

    useEffect(() => {
        keycloak
            .init({ onLoad: "login-required", checkLoginIframe: false })
            .then((auth) => {
                setAuthenticated(auth);

                // Refresh token every 60 seconds
                setInterval(() => {
                    keycloak.updateToken(70).catch(() => keycloak.logout());
                }, 60000);
            });
    }, []);

    if (!authenticated) {
        return <div>Loading...</div>;
    }

    return (
        <AuthContext.Provider value={{ keycloak }}>
            {children}
        </AuthContext.Provider>
    );
}
