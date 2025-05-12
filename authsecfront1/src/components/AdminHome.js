import React from 'react';
import { Container, Navbar, Nav, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import AdminDashboard from './AdminDashboard';

const AdminHome = ({ handleLogout }) => {
    const navigate = useNavigate();

    return (
        <div className="d-flex flex-column min-vh-100">
            {/* Navigation Bar */}
            <Navbar bg="dark" variant="dark" expand="lg" className="shadow">
                <Container fluid>
                    <Navbar.Brand href="#">
                        <i className="fas fa-crown me-2"></i>
                        Administration Panel
                    </Navbar.Brand>
                    <Navbar.Toggle aria-controls="navbarScroll" />
                    <Navbar.Collapse id="navbarScroll">
                        <Nav className="me-auto">
                            <Nav.Link onClick={() => navigate('/admin/dashboard')}>Dashboard</Nav.Link>
                            <Nav.Link onClick={() => navigate('/admin/users')}>Utilisateurs</Nav.Link>
                            <Nav.Link onClick={() => navigate('/admin/companies')}>Entreprises</Nav.Link>
                            <Nav.Link onClick={() => navigate('/admin/offers')}>Offres</Nav.Link>
                        </Nav>
                        <Nav>
                            <Button
                                variant="outline-light"
                                onClick={handleLogout}
                                className="ms-2"
                            >
                                <i className="fas fa-sign-out-alt me-1"></i> Déconnexion
                            </Button>
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>

            {/* Main Content */}
            <main className="flex-grow-1" style={{
                backgroundColor: '#f8f9fc',
                overflow: 'hidden'
            }}>
                <Container fluid className="h-100 p-0">
                    <AdminDashboard />
                </Container>
            </main>

            {/* Footer */}
            <footer className="bg-dark text-white py-3">
                <Container fluid>
                    <div className="d-flex justify-content-between align-items-center">
                        <div>
                            <span className="me-3">© 2023 Plateforme de Stages</span>
                            <a href="#" className="text-white me-3">Aide</a>
                            <a href="#" className="text-white">Confidentialité</a>
                        </div>
                        <div>
                            <span>Version 1.0.0</span>
                        </div>
                    </div>
                </Container>
            </footer>
        </div>
    );
};

export default AdminHome;