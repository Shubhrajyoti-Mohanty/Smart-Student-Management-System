// Authentication Check
function checkAuth() {
    const isLoginPath = window.location.pathname.endsWith('login.html') || window.location.pathname === '/';
    const isLoggedIn = sessionStorage.getItem('adminToken') !== null;

    if (!isLoggedIn && !isLoginPath) {
        window.location.href = 'login.html';
    } else if (isLoggedIn && isLoginPath) {
        window.location.href = 'dashboard.html';
    }
}

function logout() {
    sessionStorage.removeItem('adminToken');
    window.location.href = 'login.html';
}

// Utility: Show Alerts
function showAlert(elementId, type, message) {
    const alertEl = document.getElementById(elementId);
    if (!alertEl) return;
    
    alertEl.className = `alert ${type}`;
    alertEl.innerText = message;
    alertEl.style.display = 'block';

    setTimeout(() => {
        alertEl.style.display = 'none';
    }, 5000);
}

// Handle Login
const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        const payload = {
            username: username,
            password: password
        };

        fetch('/api/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        })
        .then(res => res.json())
        .then(data => {
            if (data.status === 'success') {
                sessionStorage.setItem('adminToken', data.token);
                window.location.href = 'dashboard.html';
            } else {
                showAlert('loginAlert', 'error', data.message || 'Login failed');
            }
        })
        .catch(err => {
            console.error(err);
            showAlert('loginAlert', 'error', 'Server error during login.');
        });
    });
}

// Handle Add/Update Student
const addStudentForm = document.getElementById('addStudentForm');
if (addStudentForm) {
    addStudentForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const id = document.getElementById('studentId').value;
        const name = document.getElementById('name').value;
        const age = document.getElementById('age').value;
        const gender = document.getElementById('gender').value;
        const course = document.getElementById('course').value;
        const attendance = document.getElementById('attendance').value;
        const marks = document.getElementById('marks').value;

        // Client-side Validation
        if (!name || !age || !gender || !course || !attendance || !marks) {
            showAlert('studentAlert', 'error', 'All fields are required.');
            return;
        }
        if (attendance < 0 || attendance > 100) {
            showAlert('studentAlert', 'error', 'Attendance must be between 0 and 100.');
            return;
        }
        if (marks < 0 || marks > 100) {
            showAlert('studentAlert', 'error', 'Marks must be between 0 and 100.');
            return;
        }

        const payload = {
            name: name,
            age: age,
            gender: gender,
            course: course,
            attendance: attendance,
            marks: marks
        };

        let method = 'POST';
        if (id) {
            payload.id = id;
            method = 'PUT';
        }

        fetch('/api/students', {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        })
        .then(res => res.json())
        .then(data => {
            if (data.status === 'success') {
                showAlert('studentAlert', 'success', data.message);
                if(method === 'POST') addStudentForm.reset(); // Reset only on Add
                setTimeout(() => {
                    window.location.href = 'view-students.html';
                }, 1500);
            } else {
                showAlert('studentAlert', 'error', data.message || 'Operation failed');
            }
        })
        .catch(err => {
            console.error(err);
            showAlert('studentAlert', 'error', 'Server error.');
        });
    });
}

// Load Students for Table
function loadStudents() {
    const tbody = document.querySelector('#studentsTable tbody');
    if (!tbody) return;

    fetch('/api/students')
        .then(res => res.json())
        .then(data => {
            tbody.innerHTML = '';
            if (data.length === 0) {
                tbody.innerHTML = '<tr><td colspan="9" style="text-align: center; color: var(--text-muted);">No student records found.</td></tr>';
                return;
            }

            data.forEach(student => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>#${student.id}</td>
                    <td style="font-weight: 500; color: var(--text-main);">${student.name}</td>
                    <td>${student.age}</td>
                    <td>${student.gender || 'N/A'}</td>
                    <td>${student.course}</td>
                    <td>${student.attendance}%</td>
                    <td>${student.marks}</td>
                    <td><span class="badge badge-${student.grade}">${student.grade}</span></td>
                    <td class="action-btns">
                        <button class="btn btn-secondary btn-sm" onclick="editStudent(${student.id})" style="padding: 0.25rem 0.5rem;">Edit</button>
                        <button class="btn btn-danger btn-sm" onclick="deleteStudent(${student.id})" style="padding: 0.25rem 0.5rem;">Delete</button>
                    </td>
                `;
                tbody.appendChild(tr);
            });
        })
        .catch(err => {
            console.error(err);
            showAlert('tableAlert', 'error', 'Failed to load student data.');
        });
}

function editStudent(id) {
    window.location.href = `add-student.html?edit=${id}`;
}

function deleteStudent(id) {
    if (confirm('Are you sure you want to delete this student record?')) {
        fetch(`/api/students?id=${id}`, {
            method: 'DELETE'
        })
        .then(res => res.json())
        .then(data => {
            if (data.status === 'success') {
                showAlert('tableAlert', 'success', data.message);
                loadStudents(); // Reload table
            } else {
                showAlert('tableAlert', 'error', data.message || 'Delete failed');
            }
        })
        .catch(err => {
            console.error(err);
            showAlert('tableAlert', 'error', 'Server error during deletion.');
        });
    }
}
