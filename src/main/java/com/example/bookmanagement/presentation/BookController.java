package com.example.bookmanagement.presentation;

import com.example.bookmanagement.application.BookService;
import com.example.bookmanagement.domain.Book;
import com.example.bookmanagement.dto.BookFormDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public String getAllBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "bookList";
    }

    @GetMapping("/{id}")
    public String getBookDetail(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id).orElseThrow(() -> new IllegalArgumentException("Book not found"));
        bookService.updateBook(id, book.getTitle(), book.getAuthor(), book.getDescription()); // 조회수 증가
        model.addAttribute("book", book);
        return "bookDetail";
    }

    @GetMapping("/new")
    public String createBookForm(Model model) {
        model.addAttribute("bookForm", new BookFormDto());
        return "bookWrite";
    }

    @PostMapping
    public String createBook(@ModelAttribute Book book) {
        bookService.createBook(book);
        return "redirect:/books";
    }

    @PostMapping("/{id}/update")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book) {
        bookService.updateBook(id, book.getTitle(), book.getAuthor(), book.getDescription());
        return "redirect:/books/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}
