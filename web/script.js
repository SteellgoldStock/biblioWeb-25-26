const API_BASE_URL = 'http://localhost:8080';

// État de l'application
let currentUser = null;
let isAdmin = false;

// Initialisation au chargement
window.addEventListener('load', () => {
    setupLoginTabs();
    setupMainTabs();
});

// Configuration des onglets de connexion
function setupLoginTabs() {
    document.querySelectorAll('.login-tab-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const mode = btn.dataset.mode;
            
            document.querySelectorAll('.login-tab-btn').forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            
            document.getElementById('userMode').classList.toggle('hidden', mode !== 'user');
            document.getElementById('adminMode').classList.toggle('hidden', mode !== 'admin');
        });
    });
}

// Configuration des onglets principaux
function setupMainTabs() {
    document.addEventListener('click', (e) => {
        if (e.target.classList.contains('tab-btn')) {
            const tabName = e.target.dataset.tab;
            
            // Activer l'onglet
            document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
            e.target.classList.add('active');
            
            // Afficher le contenu
            document.querySelectorAll('.tab-content').forEach(content => {
                content.classList.add('hidden');
            });
            document.getElementById(tabName).classList.remove('hidden');
            
            // Charger les données selon l'onglet
            if (tabName === 'catalog') loadCatalog();
            if (tabName === 'myLoans') loadMyLoans();
        }
    });
}

// Notifications
function showNotification(message, type = 'info') {
    const notification = document.getElementById('notification');
    notification.textContent = message;
    notification.className = `notification ${type}`;
    
    setTimeout(() => {
        notification.classList.add('hidden');
    }, 3000);
}

// Fonction générique pour les requêtes API
async function apiRequest(endpoint, method = 'GET', body = null) {
    try {
        const options = {
            method,
            headers: {
                'Content-Type': 'application/json'
            }
        };
        
        if (body) {
            options.body = JSON.stringify(body);
        }
        
        const response = await fetch(`${API_BASE_URL}${endpoint}`, options);
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        // Pour les requêtes DELETE qui retournent 204 No Content
        if (response.status === 204) {
            return null;
        }
        
        return await response.json();
    } catch (error) {
        console.error('API Error:', error);
        showNotification(`Erreur: ${error.message}`, 'error');
        throw error;
    }
}

// BOOKS
async function getAllBooks() {
    const books = await apiRequest('/books');
    displayBooks(books);
}

async function searchBooks() {
    const title = document.getElementById('searchBooks').value;
    if (!title) {
        showNotification('Veuillez entrer un titre', 'error');
        return;
    }
    const books = await apiRequest(`/books/search?title=${encodeURIComponent(title)}`);
    displayBooks(books);
}

async function createBook() {
    const title = document.getElementById('bookTitle').value;
    const isbn = document.getElementById('bookISBN').value;
    const authorId = document.getElementById('bookAuthorId').value;
    
    if (!title || !isbn || !authorId) {
        showNotification('Tous les champs sont requis', 'error');
        return;
    }
    
    const book = {
        title,
        ISBN: parseInt(isbn),
        author: { id: authorId }
    };
    
    const result = await apiRequest('/books', 'POST', [book]);
    showNotification(`${result} livre(s) créé(s)`, 'success');
    
    // Réinitialiser le formulaire
    document.getElementById('bookTitle').value = '';
    document.getElementById('bookISBN').value = '';
    document.getElementById('bookAuthorId').value = '';
    
    getAllBooks();
}

async function deleteBook(id) {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce livre ?')) {
        await apiRequest(`/books/${id}`, 'DELETE');
        showNotification('Livre supprimé', 'success');
        getAllBooks();
    }
}

async function checkBookAvailability(id) {
    const result = await apiRequest(`/books/${id}/availability`);
    showNotification(
        result.available ? 'Livre disponible' : 'Livre non disponible',
        result.available ? 'success' : 'info'
    );
}

function displayBooks(books) {
    const container = document.getElementById('booksResult');
    
    if (!books || books.length === 0) {
        container.innerHTML = '<p>Aucun livre trouvé</p>';
        return;
    }
    
    container.innerHTML = '<h3>Résultats</h3>';
    books.forEach(book => {
        const bookTitle = book.name || book.title || 'Titre inconnu';
        const authorName = book.author?.name || `${book.author?.firstName || ''} ${book.author?.lastName || ''}`.trim() || 'Non spécifié';
        const isbn = book.isbn || book.ISBN;
        
        const card = document.createElement('div');
        card.className = 'card';
        card.innerHTML = `
            <h4>${bookTitle}</h4>
            <p><strong>ISBN:</strong> ${isbn}</p>
            <p><strong>Auteur:</strong> ${authorName}</p>
            <p class="id">ID: ${book.id}</p>
            <button class="info-btn" onclick="checkBookAvailability('${book.id}')">Disponibilité</button>
            <button onclick="deleteBook('${book.id}')">Supprimer</button>
        `;
        container.appendChild(card);
    });
}

// MEMBERS
async function getAllMembers() {
    const members = await apiRequest('/members');
    displayMembers(members);
}

async function searchMembers() {
    const lastName = document.getElementById('searchMembers').value;
    if (!lastName) {
        showNotification('Veuillez entrer un nom', 'error');
        return;
    }
    const members = await apiRequest(`/members/search?lastName=${encodeURIComponent(lastName)}`);
    displayMembers(members);
}

async function createMember() {
    const firstName = document.getElementById('memberFirstName').value;
    const lastName = document.getElementById('memberLastName').value;
    const email = document.getElementById('memberEmail').value;
    
    if (!firstName || !lastName || !email) {
        showNotification('Tous les champs sont requis', 'error');
        return;
    }
    
    const member = { firstName, lastName, email };
    
    await apiRequest('/members', 'POST', member);
    showNotification('Membre créé', 'success');
    
    // Réinitialiser le formulaire
    document.getElementById('memberFirstName').value = '';
    document.getElementById('memberLastName').value = '';
    document.getElementById('memberEmail').value = '';
    
    getAllMembers();
}

async function deleteMember(id) {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce membre ?')) {
        await apiRequest(`/members/${id}`, 'DELETE');
        showNotification('Membre supprimé', 'success');
        getAllMembers();
    }
}

async function showMemberLoans(id) {
    const loans = await apiRequest(`/members/${id}/loans`);
    showNotification(`Ce membre a ${loans.length} emprunt(s)`, 'info');
    displayLoans(loans);
}

function displayMembers(members) {
    const container = document.getElementById('membersResult');
    
    if (!members || members.length === 0) {
        container.innerHTML = '<p>Aucun membre trouvé</p>';
        return;
    }
    
    container.innerHTML = '<h3>Résultats</h3>';
    members.forEach(member => {
        const card = document.createElement('div');
        card.className = 'card';
        card.innerHTML = `
            <h4>${member.firstName} ${member.lastName}</h4>
            <p><strong>Email:</strong> ${member.email}</p>
            <p class="id">ID: ${member.id}</p>
            <button class="info-btn" onclick="showMemberLoans('${member.id}')">Voir emprunts</button>
            <button onclick="deleteMember('${member.id}')">Supprimer</button>
        `;
        container.appendChild(card);
    });
}

// LOANS
async function getAllLoans() {
    const loans = await apiRequest('/loans');
    displayLoans(loans);
}

async function createLoan() {
    const memberId = document.getElementById('loanMemberId').value;
    const bookId = document.getElementById('loanBookId').value;
    
    if (!memberId || !bookId) {
        showNotification('Tous les champs sont requis', 'error');
        return;
    }
    
    await apiRequest('/loans', 'POST', { memberId, bookId });
    showNotification('Emprunt créé', 'success');
    
    // Réinitialiser le formulaire
    document.getElementById('loanMemberId').value = '';
    document.getElementById('loanBookId').value = '';
    
    getAllLoans();
}

async function returnLoan() {
    const loanId = document.getElementById('returnLoanId').value;
    
    if (!loanId) {
        showNotification('Veuillez entrer un ID d\'emprunt', 'error');
        return;
    }
    
    await apiRequest(`/loans/${loanId}/return`, 'PUT');
    showNotification('Livre retourné', 'success');
    
    document.getElementById('returnLoanId').value = '';
    getAllLoans();
}

async function deleteLoan(id) {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet emprunt ?')) {
        await apiRequest(`/loans/${id}`, 'DELETE');
        showNotification('Emprunt supprimé', 'success');
        getAllLoans();
    }
}

function displayLoans(loans) {
    const container = document.getElementById('loansResult');
    
    if (!loans || loans.length === 0) {
        container.innerHTML = '<p>Aucun emprunt trouvé</p>';
        return;
    }
    
    container.innerHTML = '<h3>Résultats</h3>';
    loans.forEach(loan => {
        const status = loan.returnDate ? 'returned' : 'active';
        const statusText = loan.returnDate ? 'Retourné' : 'Actif';
        const bookTitle = loan.book?.name || loan.book?.title || 'Non spécifié';
        
        const card = document.createElement('div');
        card.className = 'card';
        card.innerHTML = `
            <h4>Emprunt <span class="badge ${status}">${statusText}</span></h4>
            <p><strong>Livre:</strong> ${bookTitle}</p>
            <p><strong>Membre:</strong> ${loan.member?.firstName || ''} ${loan.member?.lastName || 'Non spécifié'}</p>
            <p><strong>Date d'emprunt:</strong> ${new Date(loan.loanDate).toLocaleDateString('fr-FR')}</p>
            ${loan.returnDate ? `<p><strong>Date de retour:</strong> ${new Date(loan.returnDate).toLocaleDateString('fr-FR')}</p>` : ''}
            <p class="id">ID: ${loan.id}</p>
            ${!loan.returnDate ? `<button class="info-btn" onclick="returnLoan(); document.getElementById('returnLoanId').value='${loan.id}'">Retourner</button>` : ''}
            <button onclick="deleteLoan('${loan.id}')">Supprimer</button>
        `;
        container.appendChild(card);
    });
}

// AUTHORS
async function getAllAuthors() {
    const authors = await apiRequest('/authors');
    displayAuthors(authors);
}

async function searchAuthors() {
    const name = document.getElementById('searchAuthors').value;
    if (!name) {
        showNotification('Veuillez entrer un nom', 'error');
        return;
    }
    const authors = await apiRequest(`/authors/search?name=${encodeURIComponent(name)}`);
    displayAuthors(authors);
}

async function createAuthor() {
    const firstName = document.getElementById('authorFirstName').value;
    const lastName = document.getElementById('authorLastName').value;
    
    if (!firstName || !lastName) {
        showNotification('Tous les champs sont requis', 'error');
        return;
    }
    
    const author = { firstName, lastName };
    
    await apiRequest('/authors', 'POST', author);
    showNotification('Auteur créé', 'success');
    
    // Réinitialiser le formulaire
    document.getElementById('authorFirstName').value = '';
    document.getElementById('authorLastName').value = '';
    
    getAllAuthors();
}

async function deleteAuthor(id) {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet auteur ?')) {
        await apiRequest(`/authors/${id}`, 'DELETE');
        showNotification('Auteur supprimé', 'success');
        getAllAuthors();
    }
}

async function showAuthorBooks(id) {
    const books = await apiRequest(`/authors/${id}/books`);
    showNotification(`Cet auteur a ${books.length} livre(s)`, 'info');
    displayBooks(books);
}

function displayAuthors(authors) {
    const container = document.getElementById('authorsResult');
    
    if (!authors || authors.length === 0) {
        container.innerHTML = '<p>Aucun auteur trouvé</p>';
        return;
    }
    
    container.innerHTML = '<h3>Résultats</h3>';
    authors.forEach(author => {
        const authorName = author.name || `${author.firstName || ''} ${author.lastName || ''}`.trim() || 'Nom inconnu';
        
        const card = document.createElement('div');
        card.className = 'card';
        card.innerHTML = `
            <h4>${authorName}</h4>
            <p class="id">ID: ${author.id}</p>
            <button class="info-btn" onclick="showAuthorBooks('${author.id}')">Voir livres</button>
            <button onclick="deleteAuthor('${author.id}')">Supprimer</button>
        `;
        container.appendChild(card);
    });
}

// Charger les données au démarrage
window.addEventListener('load', () => {
    setupLoginTabs();
    setupMainTabs();
});

// === CONNEXION ===

async function searchMemberByEmail() {
    const email = document.getElementById('loginEmail').value.trim();
    if (!email) {
        showNotification('Veuillez entrer un email', 'error');
        return;
    }
    
    try {
        const member = await apiRequest(`/members/email/${encodeURIComponent(email)}`);
        displayMemberForLogin([member]);
    } catch (error) {
        showNotification('Aucun membre trouvé avec cet email', 'error');
        const members = await apiRequest('/members');
        displayMemberForLogin(members);
    }
}

function displayMemberForLogin(members) {
    const container = document.getElementById('membersList');
    
    if (!members || members.length === 0) {
        container.innerHTML = '<p style="color: #666; text-align: center;">Aucun membre trouvé</p>';
        return;
    }
    
    container.innerHTML = '<h4 style="margin-bottom: 10px;">Sélectionnez votre compte:</h4>';
    members.forEach(member => {
        const item = document.createElement('div');
        item.className = 'member-item';
        item.onclick = () => loginAsMember(member);
        item.innerHTML = `
            <h4>${member.firstName} ${member.lastName}</h4>
            <p>${member.email}</p>
        `;
        container.appendChild(item);
    });
}

async function createAndLoginMember() {
    const firstName = document.getElementById('newMemberFirstName').value.trim();
    const lastName = document.getElementById('newMemberLastName').value.trim();
    const email = document.getElementById('newMemberEmail').value.trim();
    
    if (!firstName || !lastName || !email) {
        showNotification('Tous les champs sont requis', 'error');
        return;
    }
    
    try {
        const member = await apiRequest('/members', 'POST', { firstName, lastName, email });
        showNotification('Compte créé avec succès!', 'success');
        loginAsMember(member);
    } catch (error) {
        showNotification('Erreur lors de la création du compte', 'error');
    }
}

function loginAsMember(member) {
    currentUser = member;
    isAdmin = false;
    showMainApp();
}

function loginAsAdmin() {
    currentUser = { firstName: 'Admin', lastName: 'Système' };
    isAdmin = true;
    showMainApp();
}

function showMainApp() {
    document.getElementById('loginScreen').classList.add('hidden');
    document.getElementById('mainApp').classList.remove('hidden');
    
    // Afficher les infos utilisateur
    const userInfo = document.getElementById('userInfo');
    if (isAdmin) {
        userInfo.textContent = 'Mode Administrateur';
    } else {
        userInfo.textContent = `Connecté en tant que ${currentUser.firstName} ${currentUser.lastName}`;
    }
    
    // Afficher la bonne navigation
    if (isAdmin) {
        document.getElementById('userNav').classList.add('hidden');
        document.getElementById('adminNav').classList.remove('hidden');
        // Charger les données admin
        getAllBooks();
    } else {
        document.getElementById('adminNav').classList.add('hidden');
        document.getElementById('userNav').classList.remove('hidden');
        // Charger le catalogue
        loadCatalog();
    }
}

function logout() {
    currentUser = null;
    isAdmin = false;
    
    // Réinitialiser les formulaires de connexion
    document.getElementById('loginEmail').value = '';
    document.getElementById('newMemberFirstName').value = '';
    document.getElementById('newMemberLastName').value = '';
    document.getElementById('newMemberEmail').value = '';
    document.getElementById('membersList').innerHTML = '';
    
    // Retour à l'écran de connexion
    document.getElementById('mainApp').classList.add('hidden');
    document.getElementById('loginScreen').classList.remove('hidden');
    
    showNotification('Déconnexion réussie', 'info');
}

// === MODE UTILISATEUR ===

async function loadCatalog() {
    const books = await apiRequest('/books');
    displayCatalog(books);
}

async function searchCatalog() {
    const title = document.getElementById('catalogSearch').value.trim();
    if (!title) {
        loadCatalog();
        return;
    }
    const books = await apiRequest(`/books/search?title=${encodeURIComponent(title)}`);
    displayCatalog(books);
}

async function displayCatalog(books) {
    const container = document.getElementById('catalogResult');
    
    if (!books || books.length === 0) {
        container.innerHTML = '<p>Aucun livre trouvé</p>';
        return;
    }
    
    container.innerHTML = '';
    
    for (const book of books) {
        // Vérifier la disponibilité
        const availability = await apiRequest(`/books/${book.id}/availability`);
        const isAvailable = availability.available;
        
        // Vérifier si l'utilisateur a déjà emprunté ce livre
        let currentLoan = null;
        if (currentUser) {
            const activeLoans = await apiRequest(`/members/${currentUser.id}/loans/active`);
            currentLoan = activeLoans.find(loan => loan.book.id === book.id);
        }
        
        const bookTitle = book.name || book.title || 'Titre inconnu';
        const authorName = book.author?.name || `${book.author?.firstName || ''} ${book.author?.lastName || ''}`.trim() || 'Auteur inconnu';
        
        const card = document.createElement('div');
        card.className = 'book-card';
        card.innerHTML = `
            <h4>${bookTitle}</h4>
            <p class="author">par ${authorName}</p>
            <p class="isbn">ISBN: ${book.isbn || book.ISBN}</p>
            <div class="status">
                <span class="badge ${isAvailable ? 'available' : 'unavailable'}">
                    ${isAvailable ? 'Disponible' : 'Indisponible'}
                </span>
            </div>
            ${currentLoan 
                ? `<button class="return-btn" onclick="returnMyBook('${currentLoan.id}')">Retourner ce livre</button>`
                : `<button class="borrow-btn" ${!isAvailable ? 'disabled' : ''} onclick="borrowBook('${book.id}')">
                    ${isAvailable ? 'Emprunter' : 'Non disponible'}
                   </button>`
            }
        `;
        container.appendChild(card);
    }
}

async function borrowBook(bookId) {
    if (!currentUser) {
        showNotification('Vous devez être connecté', 'error');
        return;
    }
    
    try {
        await apiRequest('/loans', 'POST', {
            memberId: currentUser.id,
            bookId: bookId
        });
        showNotification('Livre emprunté avec succès!', 'success');
        loadCatalog();
    } catch (error) {
        showNotification('Erreur lors de l\'emprunt', 'error');
    }
}

async function returnMyBook(loanId) {
    try {
        await apiRequest(`/loans/${loanId}/return`, 'PUT');
        showNotification('Livre retourné avec succès!', 'success');
        loadCatalog();
        loadMyLoans();
    } catch (error) {
        showNotification('Erreur lors du retour', 'error');
    }
}

async function loadMyLoans() {
    if (!currentUser) return;
    
    const loans = await apiRequest(`/members/${currentUser.id}/loans`);
    displayMyLoans(loans);
}

function displayMyLoans(loans) {
    const container = document.getElementById('myLoansResult');
    
    if (!loans || loans.length === 0) {
        container.innerHTML = '<p>Vous n\'avez aucun emprunt</p>';
        return;
    }
    
    const activeLoans = loans.filter(loan => !loan.returnDate);
    const pastLoans = loans.filter(loan => loan.returnDate);
    
    container.innerHTML = `
        <h3>Emprunts en cours (${activeLoans.length})</h3>
    `;
    
    if (activeLoans.length === 0) {
        container.innerHTML += '<p>Aucun emprunt en cours</p>';
    } else {
        activeLoans.forEach(loan => {
            const bookTitle = loan.book?.name || loan.book?.title || 'Livre inconnu';
            const authorName = loan.book?.author?.name || `${loan.book?.author?.firstName || ''} ${loan.book?.author?.lastName || ''}`.trim() || 'Inconnu';
            
            const card = document.createElement('div');
            card.className = 'card';
            card.innerHTML = `
                <h4>${bookTitle} <span class="badge active">En cours</span></h4>
                <p><strong>Auteur:</strong> ${authorName}</p>
                <p><strong>Emprunté le:</strong> ${new Date(loan.loanDate).toLocaleDateString('fr-FR')}</p>
                <button class="return-btn" onclick="returnMyBook('${loan.id}')">Retourner</button>
            `;
            container.appendChild(card);
        });
    }
    
    if (pastLoans.length > 0) {
        container.innerHTML += `<h3 style="margin-top: 30px;">Historique (${pastLoans.length})</h3>`;
        pastLoans.forEach(loan => {
            const bookTitle = loan.book?.name || loan.book?.title || 'Livre inconnu';
            const authorName = loan.book?.author?.name || `${loan.book?.author?.firstName || ''} ${loan.book?.author?.lastName || ''}`.trim() || 'Inconnu';
            
            const card = document.createElement('div');
            card.className = 'card';
            card.innerHTML = `
                <h4>${bookTitle} <span class="badge returned">Retourné</span></h4>
                <p><strong>Auteur:</strong> ${authorName}</p>
                <p><strong>Emprunté le:</strong> ${new Date(loan.loanDate).toLocaleDateString('fr-FR')}</p>
                <p><strong>Retourné le:</strong> ${new Date(loan.returnDate).toLocaleDateString('fr-FR')}</p>
            `;
            container.appendChild(card);
        });
    }
}
