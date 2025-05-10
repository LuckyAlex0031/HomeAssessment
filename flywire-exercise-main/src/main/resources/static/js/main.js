document.addEventListener('DOMContentLoaded', function() {
    loadEmployees();

    // Load employees when page loads
    function loadEmployees() {
        fetch('/api/employees')
            .then(response => response.json())
            .then(data => {
                displayEmployees(data)
            })
            .catch(error => console.error('Error:', error));
    }

    // Display employees in the table
    function displayEmployees(employees) {
        const employeeList = document.getElementById('employeeList');
        employeeList.innerHTML = '';

        employees.forEach(employee => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${employee.name}</td>
                <td>${employee.position}</td>
                <td>${employee.manager}</td>
                <td>${new Date(employee.dateHired).toLocaleDateString()}</td>
                <td><span class="${employee.active ? 'status-active' : 'status-inactive'}">
                    ${employee.active ? 'Active' : 'Inactive'}</span></td>
                <td>
                    <button class="btn btn-sm btn-deactivate" onclick="deactivateEmployee('${employee.id}')">
                        <i class="fas fa-user-times"></i>
                    </button>
                </td>
            `;
            employeeList.appendChild(row);
        });
    }

    // Handle date range search
    document.getElementById('dateRangeForm').addEventListener('submit', function(e) {
        e.preventDefault();
        const startDate = document.getElementById('startDate').value;
        const endDate = document.getElementById('endDate').value;

        fetch(`/api/employees/date-range?startDate=${startDate}&endDate=${endDate}`)
            .then(response => response.json())
            .then(data => displayEmployees(data))
            .catch(error => console.error('Error:', error));
    });

    // Handle employee creation
    document.getElementById('saveEmployee').addEventListener('click', function() {
        const employee = {
            name: document.getElementById('name').value,
            position: document.getElementById('position').value,
            manager: document.getElementById('manager').value,
            dateHired: document.getElementById('dateHired').value,
            active: true
        };

        fetch('/api/employees', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(employee)
        })
        .then(response => {
            if (response.ok) {
                loadEmployees();
                document.getElementById('addEmployeeModal').querySelector('.btn-close').click();
                document.getElementById('addEmployeeForm').reset();
            }
        })
        .catch(error => console.error('Error:', error));
    });

    // Handle employee deactivation
    function deactivateEmployee(id) {
        if (confirm('Are you sure you want to deactivate this employee?')) {
            fetch(`/api/employees/${id}/deactivate`, {
                method: 'PUT'
            })
            .then(response => {
                if (response.ok) {
                    loadEmployees();
                }
            })
            .catch(error => console.error('Error:', error));
        }
    }
});
