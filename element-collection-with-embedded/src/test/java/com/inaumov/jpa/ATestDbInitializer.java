package com.inaumov.jpa;

import lombok.extern.slf4j.Slf4j;
import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public abstract class ATestDbInitializer {

    private static final String PERSISTENCE_UNIT_NAME = "jpa-element-collection-with-embedded-PU";
    EntityManager entityManager;

    @Before
    public void setUp() {
        log.info("Starting in-memory database for unit tests");
        try {
            EntityManagerFactory emFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            entityManager = emFactory.createEntityManager();

            //Loads the data set from a file named dataset.xml
            FlatXmlDataSet flatXmlDataSet = new FlatXmlDataSetBuilder()
                    .build(
                            Thread.currentThread()
                                    .getContextClassLoader()
                                    .getResourceAsStream("dataset.xml"));
            //Clean the data from previous test and insert new data test.
            DatabaseOperation.CLEAN_INSERT.execute(getDatabaseConnection(), flatXmlDataSet);
        } catch (SQLException | DatabaseUnitException e) {
            log.error("Database set up has been failed...", e);
        }
        log.info("Database set up has been done...");
        entityManager.getTransaction().begin();
    }

    private DatabaseConnection getDatabaseConnection() throws DatabaseUnitException {
        Connection conn = entityManager.unwrap(Connection.class);
        return new DatabaseConnection(conn);
    }

    void assertTablesByQuery(String expectedDataSetName, String sqlQuery, String table) throws Exception {
        FlatXmlDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                .build(
                        Thread.currentThread()
                                .getContextClassLoader()
                                .getResourceAsStream(expectedDataSetName));

        Assertion.assertEqualsByQuery(expectedDataSet, getDatabaseConnection(), sqlQuery, table, new String[]{});
    }

    void commitTransaction() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().commit();
        }
    }

    @After
    public void tearDown() throws Exception {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

}