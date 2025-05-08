import React from 'react';
import { Link } from 'react-router-dom';
import NotificationDropdown from '../components/NotificationDropdown';

const HomeCompany = () => {
    const userId = parseInt(localStorage.getItem("userId"));

    return (
        <div>
            <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                padding: '1rem',
                borderBottom: '1px solid #eee'
            }}>
                <div style={{ display: 'flex', gap: '1rem' }}>
                    <Link to="/company-profile" style={{ textDecoration: 'none', color: '#1976d2' }}>
                        Voir mon profil entreprise
                    </Link>
                    <Link to="/offers" style={{ textDecoration: 'none', color: '#1976d2' }}>
                        Gérer mes Offres
                    </Link>
                </div>

                {userId && <NotificationDropdown userId={userId} />}
            </div>

            {/* Contenu principal */}
            <div style={{ padding: '2rem' }}>
                <h1>🏢 Espace Entreprise</h1>
                <p>Bienvenue sur la page réservée aux utilisateurs avec le rôle <strong>MANAGER</strong>.</p>
            </div>
        </div>
    );
};

export default HomeCompany;