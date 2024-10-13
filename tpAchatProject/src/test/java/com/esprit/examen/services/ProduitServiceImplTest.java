package com.esprit.examen.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.esprit.examen.entities.CategorieProduit;
import com.esprit.examen.entities.Produit;
import com.esprit.examen.entities.Stock;
import com.esprit.examen.repositories.ProduitRepository;

@ExtendWith(MockitoExtension.class)
public class ProduitServiceImplTest {

    @Mock
    ProduitRepository produitRepository;

    @InjectMocks
    ProduitServiceImpl produitService;

    @Test
    public void testFindProductsByStockAndCategoryWithLowQuantity() {
        // Arrange
        Stock stock = Stock.builder()
                .id(1L)  // Stock ID
                .libelleStock("Stock1")  // Stock label
                .qte(100)  // Quantity in stock (Use 'qte' field)
                .qteMin(10)  // Minimum quantity in stock
                .build();

        CategorieProduit categorie = CategorieProduit.builder()
                .idCategorieProduit(1L)  // Category ID
                .libelleCategorie("Electronics")  // Category label
                .codeCategorie("ELEC001")  // Example category code
                .build();

        Produit produitA = Produit.builder()
                .idProduit(1L)  // Product A ID
                .libelleProduit("Laptop A")  // Product A name (Use 'libelleProduit')
                .quantite(15)  // Quantity of Product A
                .prix(1000.0f)  // Price of Product A
                .stock(stock)  // Associated stock
                .categorieProduit(categorie)  // Associated category
                .build();

        Produit produitB = Produit.builder()
                .idProduit(2L)  // Product B ID
                .libelleProduit("Laptop B")  // Product B name
                .quantite(5)  // Quantity of Product B
                .prix(1500.0f)  // Price of Product B
                .stock(stock)  // Associated stock
                .categorieProduit(categorie)  // Associated category
                .build();

        Produit produitC = Produit.builder()
                .idProduit(3L)  // Product C ID
                .libelleProduit("Laptop C")  // Product C name
                .quantite(9)  // Quantity of Product C
                .prix(1200.0f)  // Price of Product C
                .stock(stock)  // Associated stock
                .categorieProduit(categorie)  // Associated category
                .build();


        List<Produit> mockProduits = Arrays.asList(produitA, produitB, produitC);

        when(produitRepository.findByStockIdAndCategorieProduitLibelleCategorie(1L, "Electronics"))
                .thenReturn(mockProduits);

        // Act
        List<Produit> result = produitService.findProductsByStockAndCategoryWithLowQuantity(1L, "Electronics");

        // Assert
        assertEquals(2, result.size()); 
        assertEquals(5, result.get(0).getQuantite());
        assertEquals(9, result.get(1).getQuantite());
    }
}
