package cz.czechitas.java2webapps.ukol6.controller;

import cz.czechitas.java2webapps.ukol6.entity.Vizitka;
import cz.czechitas.java2webapps.ukol6.repository.VizitkaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class VizitkaController {

    private final VizitkaRepository vizitkaRepository;

    @Autowired
    public VizitkaController(VizitkaRepository repository) {
        this.vizitkaRepository = repository;
    }

    @InitBinder
    public void nullStringBinding(WebDataBinder binder) {
        //prázdné textové řetězce nahradit null hodnotou
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/")
    public ModelAndView seznam() {
        return new ModelAndView("seznam")
                .addObject("osoby", vizitkaRepository.findAll());
    }

    @GetMapping("/nova")
    public ModelAndView novaVizitka() {
        return new ModelAndView("formular")
                .addObject("osoba", new Vizitka());
    }

    @PostMapping("/nova")
    public String pridat(@ModelAttribute("osoba") @Valid Vizitka osoba, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "detail";
        }
        vizitkaRepository.save(osoba);
        return "redirect:/";
    }

    @GetMapping("/{id:[0-9]+}")
    public Object detail(@PathVariable Integer id) {
        var vizitka = vizitkaRepository.findById(id);
        if (vizitka.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return new ModelAndView("vizitka")
                    .addObject("vizitka", vizitka.get());
        }
    }

    @PostMapping(value = "/editovat/{id:[0-9]+}")
    public String ulozit(@PathVariable Integer id, @ModelAttribute("osoba") @Valid Vizitka osoba, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "detail";
        }
        vizitkaRepository.save(osoba);
        return "redirect:/";
    }

    @PostMapping(value = "/{id:[0-9]+}", params = "akce=smazat")
    public String smazat(@PathVariable Integer id) {
        vizitkaRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/editovat/{id:[0-9]+}")
    public Object editovat(@PathVariable Integer id) {
        var vizitka = vizitkaRepository.findById(id);
        if (vizitka.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return new ModelAndView("formular")
                    .addObject("osoba", vizitka.get());
        }
    }

}


