/*package com.bootcamp.desafioSpring;

import com.meli.demo.linktracker.exception.InvalidLinkUrlException;
import com.meli.demo.linktracker.exception.LinkAlreadyExistsException;
import com.meli.demo.linktracker.model.LInkDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RepositoryTests {

    ILinkRepository repository;


    @BeforeEach
    void setUp() {
        repository = new LinkRepository();
    }

    @Test
    void testLinkUrlDoesntExistInEmptyRepo() {
        String url = "http://www.mercadolibre.com";

        boolean actual = repository.exists(url);

        Assertions.assertFalse(actual);
    }

    @Test
    void testLinkUrlDoesntExist() throws LinkAlreadyExistsException, InvalidLinkUrlException {
        LInkDTO link = new LInkDTO();
        link.setUrl("http://www.mercadolibre.com");
        link.setPassword("GalperinTeAmo");
        repository.add(link);

        boolean actual = repository.exists("http://amazon.com");

        Assertions.assertFalse(actual);
    }

    @Test
    void testLinkUrlExists() throws LinkAlreadyExistsException, InvalidLinkUrlException {
        LInkDTO link = new LInkDTO();
        link.setUrl("http://www.mercadolibre.com");
        link.setPassword("GalperinTeAmo");
        repository.add(link);

        boolean actual = repository.exists(link.getUrl());

        Assertions.assertTrue(actual);
    }

    @Test
    void testAddNullLink() throws LinkAlreadyExistsException {
        Assertions.assertDoesNotThrow(() -> repository.add(null));
    }

    @Test
    void testAddMalformedUrlLink() throws LinkAlreadyExistsException, InvalidLinkUrlException {
        LInkDTO newLink = new LInkDTO();
        newLink.setUrl("htpp:/www.mercadolibre.com");
        newLink.setPassword("GalperinTeAmo");
        Assertions.assertThrows(InvalidLinkUrlException.class, () -> repository.add(newLink));;
    }

    @Test
    void testAddNewLink() throws LinkAlreadyExistsException, InvalidLinkUrlException {
        LInkDTO newLink = new LInkDTO();
        newLink.setUrl("http://www.mercadolibre.com");
        newLink.setPassword("GalperinTeAmo");

        repository.add(newLink);

        LInkDTO actual = repository.findByUrl(newLink.getUrl());

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(repository.exists(newLink.getUrl()));

        Assertions.assertTrue(Integer.parseInt(actual.getId()) >= 0);
        Assertions.assertEquals(actual.getUrl(), newLink.getUrl());
        Assertions.assertEquals(actual.getPassword(), newLink.getPassword());
        Assertions.assertTrue(actual.getRedirects() == 0);
    }

    @Test
    void testAddDuplicatedLink() throws LinkAlreadyExistsException, InvalidLinkUrlException {
        LInkDTO newLink = new LInkDTO();
        newLink.setUrl("http://www.mercadolibre.com");
        newLink.setPassword("GalperinTeAmo");

        repository.add(newLink);

        Assertions.assertThrows(LinkAlreadyExistsException.class, () -> repository.add(newLink));;
    }

    @Test
    void testRemoveNullLink() {
        Assertions.assertDoesNotThrow(() -> repository.remove(null));
    }

    @Test
    void testRemoveUncreatedLink() {
        LInkDTO newLink = new LInkDTO();
        newLink.setUrl("http://www.mercadolibre.com");
        newLink.setPassword("GalperinTeAmo");

        Assertions.assertDoesNotThrow(() -> repository.remove(newLink));;
        Assertions.assertFalse(repository.exists(newLink.getUrl()));
    }

    @Test
    void testRemoveCreatedLink() throws InvalidLinkUrlException, LinkAlreadyExistsException {
        LInkDTO newLink = new LInkDTO();
        newLink.setUrl("http://www.mercadolibre.com");
        newLink.setPassword("GalperinTeAmo");

        repository.add(newLink);
        Assertions.assertTrue(repository.exists(newLink.getUrl()));

        repository.remove(newLink);
        Assertions.assertFalse(repository.exists(newLink.getUrl()));
    }

    @Test
    void testFindNullByUrl() {
        Assertions.assertDoesNotThrow(() -> repository.findByUrl(null));
        Assertions.assertNull(repository.findByUrl(null));
    }

    @Test
    void testFindNotCreatedByUrl() {
        String url = "http://www.mercadolibre.com";

        Assertions.assertFalse(repository.exists(url));
        Assertions.assertNull(repository.findByUrl(url));
    }

    @Test
    void testFindCreatedLinkByUrl() throws InvalidLinkUrlException, LinkAlreadyExistsException {
        LInkDTO newLink = new LInkDTO();
        newLink.setUrl("http://www.mercadolibre.com");
        newLink.setPassword("GalperinTeAmo");

        repository.add(newLink);
        Assertions.assertTrue(repository.exists(newLink.getUrl()));

        Assertions.assertNotNull(repository.findByUrl(newLink.getUrl()));
    }

    @Test
    void testFindNullById() {
        Assertions.assertDoesNotThrow(() -> repository.findById(null));
        Assertions.assertNull(repository.findById(null));
    }

    @Test
    void testFindNotCreatedById() {
        Assertions.assertNull(repository.findById("0"));
    }

    @Test
    void testFindCreatedLinkById() throws InvalidLinkUrlException, LinkAlreadyExistsException {
        LInkDTO newLink = new LInkDTO();
        newLink.setUrl("http://www.mercadolibre.com");
        newLink.setPassword("GalperinTeAmo");

        repository.add(newLink);
        Assertions.assertTrue(repository.exists(newLink.getUrl()));

        LInkDTO found = repository.findByUrl(newLink.getUrl());

        Assertions.assertNotNull(repository.findById(found.getId()));
    }
}
*/