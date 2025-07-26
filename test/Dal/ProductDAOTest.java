/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package Dal;

import Models.Category;
import Models.Product;
import Models.Unit;
import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author duckh
 */
public class ProductDAOTest {
    
    public ProductDAOTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getAllProducts method, of class ProductDAO.
     */
    @Test
    public void testGetAllProducts_0args() {
        System.out.println("getAllProducts");
        ProductDAO instance = null;
        List<Product> expResult = null;
        List<Product> result = instance.getAllProducts();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProduct method, of class ProductDAO.
     */
    @Test
    public void testGetProduct() {
        System.out.println("getProduct");
        String sql = "";
        ProductDAO instance = null;
        Vector<Product> expResult = null;
        Vector<Product> result = instance.getProduct(sql);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllProducts method, of class ProductDAO.
     */
    @Test
    public void testGetAllProducts_4args() {
        System.out.println("getAllProducts");
        int page = 0;
        int pageSize = 0;
        String search = "";
        String categoryFilter = "";
        ProductDAO instance = null;
        List<Product> expResult = null;
        List<Product> result = instance.getAllProducts(page, pageSize, search, categoryFilter);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTotalProducts method, of class ProductDAO.
     */
    @Test
    public void testGetTotalProducts() {
        System.out.println("getTotalProducts");
        String search = "";
        String categoryFilter = "";
        ProductDAO instance = null;
        int expResult = 0;
        int result = instance.getTotalProducts(search, categoryFilter);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProductById method, of class ProductDAO.
     */
    @Test
    public void testGetProductById() {
        System.out.println("getProductById");
        int productId = 0;
        ProductDAO instance = null;
        Product expResult = null;
        Product result = instance.getProductById(productId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createProduct method, of class ProductDAO.
     */
    @Test
    public void testCreateProduct() {
        System.out.println("createProduct");
        Product product = null;
        ProductDAO instance = null;
        boolean expResult = false;
        boolean result = instance.createProduct(product);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateProduct method, of class ProductDAO.
     */
    @Test
    public void testUpdateProduct() {
        System.out.println("updateProduct");
        Product product = null;
        ProductDAO instance = null;
        boolean expResult = false;
        boolean result = instance.updateProduct(product);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteProduct method, of class ProductDAO.
     */
    @Test
    public void testDeleteProduct() {
        System.out.println("deleteProduct");
        int productId = 0;
        ProductDAO instance = null;
        boolean expResult = false;
        boolean result = instance.deleteProduct(productId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllCategories method, of class ProductDAO.
     */
    @Test
    public void testGetAllCategories() {
        System.out.println("getAllCategories");
        ProductDAO instance = null;
        List<Category> expResult = null;
        List<Category> result = instance.getAllCategories();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllUnits method, of class ProductDAO.
     */
    @Test
    public void testGetAllUnits() {
        System.out.println("getAllUnits");
        ProductDAO instance = null;
        List<Unit> expResult = null;
        List<Unit> result = instance.getAllUnits();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllProductsFiltered method, of class ProductDAO.
     */
    @Test
    public void testGetAllProductsFiltered() {
        System.out.println("getAllProductsFiltered");
        String search = "";
        String categoryID = "";
        Boolean status = null;
        BigDecimal minImportPrice = null;
        BigDecimal maxImportPrice = null;
        BigDecimal minSellingPrice = null;
        BigDecimal maxSellingPrice = null;
        ProductDAO instance = null;
        List<Product> expResult = null;
        List<Product> result = instance.getAllProductsFiltered(search, categoryID, status, minImportPrice, maxImportPrice, minSellingPrice, maxSellingPrice);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class ProductDAO.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        ProductDAO.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
