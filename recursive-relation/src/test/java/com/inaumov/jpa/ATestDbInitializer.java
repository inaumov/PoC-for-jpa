package com.inaumov.jpa;

import lombok.extern.slf4j.Slf4j;
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

    private static final String PERSISTENCE_UNIT_NAME = "jpa-recursive-relation";
    EntityManager entityManager;

    @Before
    public void setUp() {
        log.info("Starting in-memory database for unit tests");
        try {
            EntityManagerFactory emFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            entityManager = emFactory.createEntityManager();
            Connection conn = entityManager.unwrap(Connection.class);
            DatabaseConnection mDBUnitConnection = new DatabaseConnection(conn);
            //Loads the data set from a file named dataset.xml
            FlatXmlDataSet flatXmlDataSet = new FlatXmlDataSetBuilder()
                    .build(
                            Thread.currentThread()
                                    .getContextClassLoader()
                                    .getResourceAsStream("dataset.xml"));
            //Clean the data from previous test and insert new data test.
            DatabaseOperation.CLEAN_INSERT.execute(mDBUnitConnection, flatXmlDataSet);
        } catch (SQLException | DatabaseUnitException e) {
            log.error("Database set up has been done...", e);
        }
        log.info("Database set up has been done...");
        entityManager.getTransaction().begin();
    }

    @After
    public void tearDown() throws Exception {
        entityManager.getTransaction().commit();
    }

}