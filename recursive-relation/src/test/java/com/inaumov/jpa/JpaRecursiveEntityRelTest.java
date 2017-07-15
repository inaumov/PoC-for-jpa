package com.inaumov.jpa;

import com.inaumov.entities.RecursiveDepartment;
import org.junit.Test;

import static org.junit.Assert.*;

public class JpaRecursiveEntityRelTest extends ATestDbInitializer {

    private static final String DEP_MAIN_1 = "dep_main_1";
    private static final String DEP_MAIN_2 = "dep_main_2";

    private static final String DEP_SUB_1 = "dep_sub_1";
    private static final String DEP_SUB_2 = "dep_sub_2";
    private static final String DEP_SUB_3 = "dep_sub_3";

    @Test
    public void testParentHasChildren() {
        final RecursiveDepartment parent = entityManager.find(RecursiveDepartment.class, DEP_MAIN_1);
        assertNotNull(parent);
        assertEquals("Philips", parent.getName());
        assertTrue(parent.isActive());
        assertTrue(parent.getParents().isEmpty());
        assertEquals(2, parent.getChildren().size());
    }

    @Test
    public void testParentHasNoChildren() {
        final RecursiveDepartment parent = entityManager.find(RecursiveDepartment.class, DEP_MAIN_2);
        assertNotNull(parent);
        assertEquals("LG", parent.getName());
        assertFalse(parent.isActive());
        assertTrue(parent.getParents().isEmpty());
        assertTrue(parent.getChildren().isEmpty());
    }

    @Test
    public void testChildHasParents() {
        final RecursiveDepartment child1 = entityManager.find(RecursiveDepartment.class, DEP_SUB_1);
        assertChild(child1);

        final RecursiveDepartment child2 = entityManager.find(RecursiveDepartment.class, DEP_SUB_2);
        assertChild(child2);
    }

    private void assertChild(RecursiveDepartment child1) {
        assertNotNull(child1);
        assertTrue(child1.getChildren().isEmpty());
        assertEquals(1, child1.getParents().size());
    }

    @Test
    public void testAddChild() throws Exception {
        final RecursiveDepartment parent = entityManager.find(RecursiveDepartment.class, DEP_MAIN_1);
        final RecursiveDepartment newChildDepartment = new RecursiveDepartment();
        newChildDepartment.setId(DEP_SUB_3);
        newChildDepartment.setName("Mobile");
        newChildDepartment.getParents().add(parent);
        parent.getChildren().add(newChildDepartment);

        entityManager.persist(newChildDepartment); // persist, otherwise JPA will delete existed children and then insert all again
        entityManager.flush();

        // assert parent
        final RecursiveDepartment updated_parent = entityManager.find(RecursiveDepartment.class, DEP_MAIN_1);
        assertTrue(updated_parent.getParents().isEmpty());
        assertEquals(3, updated_parent.getChildren().size());

        // assert children
        final RecursiveDepartment new_child = entityManager.find(RecursiveDepartment.class, DEP_SUB_3);
        assertNotNull(new_child);
        assertTrue(new_child.getChildren().isEmpty());
        assertEquals(1, new_child.getParents().size());
    }

    @Test
    public void testAddParent() throws Exception {
        final RecursiveDepartment child = entityManager.find(RecursiveDepartment.class, DEP_SUB_1);
        final RecursiveDepartment newParentDepartment = new RecursiveDepartment();
        newParentDepartment.setId("wwe");
        newParentDepartment.setName("WWE");
        newParentDepartment.getChildren().add(child);
        child.getParents().add(newParentDepartment);

        entityManager.persist(newParentDepartment);
        entityManager.flush();

        // assert parent
        final RecursiveDepartment new_parent = entityManager.find(RecursiveDepartment.class, "wwe");
        assertTrue(new_parent.getParents().isEmpty());
        assertEquals(1, new_parent.getChildren().size());

        // assert children
        final RecursiveDepartment updated_child = entityManager.find(RecursiveDepartment.class, DEP_SUB_1);
        assertNotNull(updated_child);
        assertTrue(updated_child.getChildren().isEmpty());
        assertEquals(2, updated_child.getParents().size());
    }

    @Test
    public void testRemoveParent() throws Exception {
        entityManager.remove(entityManager.getReference(RecursiveDepartment.class, DEP_MAIN_1));
        assertNull(entityManager.find(RecursiveDepartment.class, DEP_MAIN_1));
        assertNotNull(entityManager.find(RecursiveDepartment.class, DEP_SUB_1));
        assertNotNull(entityManager.find(RecursiveDepartment.class, DEP_SUB_2));
    }

    @Test
    public void testRemoveChild() throws Exception {
        entityManager.remove(entityManager.getReference(RecursiveDepartment.class, DEP_SUB_1));
        assertNull(entityManager.find(RecursiveDepartment.class, DEP_SUB_1));
        assertNotNull(entityManager.find(RecursiveDepartment.class, DEP_MAIN_1));
    }

    @Test
    public void testUpdate_throughParent() throws Exception {

        final RecursiveDepartment parent = entityManager.find(RecursiveDepartment.class, DEP_MAIN_2);
        final RecursiveDepartment child1 = entityManager.find(RecursiveDepartment.class, DEP_SUB_1);
        final RecursiveDepartment child2 = entityManager.find(RecursiveDepartment.class, DEP_SUB_2);
        child1.getParents().add(parent);
        child2.getParents().add(parent);
        parent.getChildren().add(child1);
        parent.getChildren().add(child2);

        entityManager.merge(parent);
        entityManager.flush();

        // assert parent
        final RecursiveDepartment updated_parent = entityManager.find(RecursiveDepartment.class, DEP_MAIN_2);
        assertTrue(updated_parent.getParents().isEmpty());
        assertEquals(2, updated_parent.getChildren().size());

        // assert children
        final RecursiveDepartment updated_child1 = entityManager.find(RecursiveDepartment.class, DEP_SUB_1);
        assertNotNull(updated_child1);
        assertTrue(updated_child1.getChildren().isEmpty());
        assertEquals(2, updated_child1.getParents().size());

        final RecursiveDepartment updated_child2 = entityManager.find(RecursiveDepartment.class, DEP_SUB_2);
        assertNotNull(updated_child2);
        assertTrue(updated_child2.getChildren().isEmpty());
        assertEquals(2, updated_child2.getParents().size());
    }

    @Test
    public void testUpdate_throughChild() throws Exception {

        final RecursiveDepartment parent = entityManager.find(RecursiveDepartment.class, DEP_MAIN_2);
        final RecursiveDepartment child1 = entityManager.find(RecursiveDepartment.class, DEP_SUB_1);
        final RecursiveDepartment child2 = entityManager.find(RecursiveDepartment.class, DEP_SUB_2);
        parent.getChildren().add(child1);
        parent.getChildren().add(child2);
        child1.getParents().add(parent);
        child2.getParents().add(parent);

        entityManager.merge(child1);
        entityManager.merge(child2);
        entityManager.flush();

        // assert parent
        final RecursiveDepartment updated_parent = entityManager.find(RecursiveDepartment.class, DEP_MAIN_2);
        assertTrue(updated_parent.getParents().isEmpty());
        assertEquals(2, updated_parent.getChildren().size());

        // assert children
        final RecursiveDepartment updated_child1 = entityManager.find(RecursiveDepartment.class, DEP_SUB_1);
        assertNotNull(updated_child1);
        assertTrue(updated_child1.getChildren().isEmpty());
        assertEquals(2, updated_child1.getParents().size());

        final RecursiveDepartment updated_child2 = entityManager.find(RecursiveDepartment.class, DEP_SUB_2);
        assertNotNull(updated_child2);
        assertTrue(updated_child2.getChildren().isEmpty());
        assertEquals(2, updated_child2.getParents().size());
    }

}