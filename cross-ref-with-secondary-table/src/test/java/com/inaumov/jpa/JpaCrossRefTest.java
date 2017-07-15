package com.inaumov.jpa;

import com.inaumov.entities.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@Slf4j
public class JpaCrossRefTest extends ATestDbInitializer {

    private static final String DEPARTMENT_1 = "department_1";
    private static final String EMPLOYEE_1 = "employee_1";

    @Test
    public void testGetAllDepartments() {
        List<Department> resultList = entityManager.createQuery("select d from Department d", Department.class).getResultList();
        log.info("num of departments:" + resultList.size());
        assertThat(resultList.size(), is(3));
        for (Department next : resultList) {
            assertNotNull(next);
            log.info("next department: " + next.getName());
        }
    }

    @Test
    public void testGetAllEmployeesInDepartment() {
        List<DepartmentEmployee> resultList = entityManager
                .createQuery("select de from DepartmentEmployee de where de.departmentId = :departmentId", DepartmentEmployee.class)
                .setParameter("departmentId", DEPARTMENT_1)
                .getResultList();
        log.info("num of employees:" + resultList.size());
        // because of join
        assertThat(resultList.size(), is(4));
        for (DepartmentEmployee next : resultList) {
            assertNotNull(next);
            log.info("next employee: " + next.getName());
        }
    }

    @Test
    public void testGetByDepartmentEmployeeId() {
        DepartmentEmployeeId id = new DepartmentEmployeeId();
        id.setDepartmentId(DEPARTMENT_1);
        id.setEmployeeId(EMPLOYEE_1);
        DepartmentEmployee departmentEmployee = entityManager.find(DepartmentEmployee.class, id);
        assertNotNull(departmentEmployee);
        log.info("employee: " + departmentEmployee.getEmployeeId());
    }

    @Test
    public void testGetDepartmentByIdAndCheckItsEmployees() {
        Department department = entityManager.find(Department.class, DEPARTMENT_1);
        assertNotNull(department);
        assertThat(department.getDepartmentEmployees().size(), is(4));
        log.info("department: " + department.getDepartmentId());
        for (DepartmentEmployee next : department.getDepartmentEmployees()) {
            assertNotNull(next);
            log.info("next employee: " + next.getName());
        }
    }

    @Test
    public void testCreateNewDepartmentsWithEmployees() {
        Department johnConnor = new Department();
        johnConnor.setName("john_connor");
        entityManager.persist(johnConnor);

        DepartmentEmployeeId id = new DepartmentEmployeeId();
        id.setDepartmentId(DEPARTMENT_1);
        id.setEmployeeId(EMPLOYEE_1);
        DepartmentEmployee departmentEmployee1 = new DepartmentEmployee();
        final DepartmentEmployee departmentEmployee = entityManager.find(DepartmentEmployee.class, id);
        departmentEmployee1.setName(departmentEmployee.getName());
        departmentEmployee1.setEmployeeId(departmentEmployee.getEmployeeId());
        departmentEmployee1.setDepartmentId(johnConnor.getDepartmentId());
        departmentEmployee1.setOrderingNumber(15);
        departmentEmployee1.setDepartment(johnConnor);
//        http://www.java2s.com/Tutorial/Java/0355__JPA/SecondaryTableWithManyToOneRelationship.htm
        johnConnor.getDepartmentEmployees().add(departmentEmployee);
        entityManager.merge(johnConnor);
    }

    @Test
    public void testGetDepartmentsOfEmployee() {
        List<DepartmentEmployee> resultList = entityManager
                .createQuery("select de from DepartmentEmployee de where de.employeeId = :employeeId", DepartmentEmployee.class)
                .setParameter("employeeId", EMPLOYEE_1)
                .getResultList();
        log.info("num of department employees:" + resultList.size());

        assertThat(resultList.size(), is(2));
        for (DepartmentEmployee next : resultList) {
            assertNotNull(next);
            log.info("next department employee: " + next.getDepartment().toString());
        }
    }

    @Test
    public void testDeleteEmployee() {
        DepartmentEmployeeId id = new DepartmentEmployeeId();
        id.setDepartmentId(DEPARTMENT_1);
        id.setEmployeeId(EMPLOYEE_1);
        entityManager.remove(entityManager.find(DepartmentEmployee.class, id));

        List<DepartmentEmployee> resultList = entityManager
                .createQuery("select de from DepartmentEmployee de where de.departmentId = :departmentId", DepartmentEmployee.class)
                .setParameter("departmentId", DEPARTMENT_1)
                .getResultList();
        log.info("num of employees:" + resultList.size());
        assertThat(resultList.size(), is(3));
    }

}