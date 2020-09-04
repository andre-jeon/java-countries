package com.lambdaschool.countries.controllers;

import com.lambdaschool.countries.models.Country;
import com.lambdaschool.countries.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@RestController
public class CountryController {

    @Autowired
    private CountryRepository countryrepos;

    private List<Country> findCountries(List<Country> myList, CheckCountry tester) {
        List<Country> tempList = new ArrayList<>();

        for (Country e : myList) {
            if (tester.test(e)) {
                tempList.add(e);
            }
        }
        return tempList;
    }

    // http://localhost:2019/names/all
    @GetMapping(value = "/names/all", produces = {"application/json"})
    public ResponseEntity<?> listAllCountries() {
        List<Country> myList = new ArrayList<>();

        countryrepos.findAll().iterator().forEachRemaining(myList::add);
        myList.sort((e1, e2) -> e1.getName().compareToIgnoreCase(e2.getName()));

        return new ResponseEntity<>(myList, HttpStatus.OK);
    }

    // http://localhost:2019/names/start/u
    @GetMapping(value = "/names/start/{letter}", produces = {"application/json"})
    public ResponseEntity<?> listAllByName(@PathVariable char letter) {
        List<Country> myList = new ArrayList<>();
        countryrepos.findAll().iterator().forEachRemaining(myList::add);

        List<Country> rtnList = findCountries(myList, e -> e.getName().charAt(0) == letter);

        return new ResponseEntity<>(rtnList, HttpStatus.OK);
    }

    // http://localhost:2019/population/total
    @GetMapping(value = "/population/total", produces = {"application/json"})
    public ResponseEntity<?> displayPopulation() {
        List<Country> myList = new ArrayList<>();

        countryrepos.findAll().iterator().forEachRemaining(myList::add);

        long total = 0;
        for (Country e : myList) {
            total = total + e.getPopulation();
        }

        System.out.println("The Total Population is " + total);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // http://localhost:2019/population/min
    @GetMapping(value = "/population/min", produces = {"application/json"})
    public ResponseEntity<?> displayPopulationSmallest() {
        List<Country> myList = new ArrayList<>();

        countryrepos.findAll().iterator().forEachRemaining(myList::add);

        myList.sort(Comparator.comparing(Country::getPopulation));
        return new ResponseEntity<>(myList.get(0), HttpStatus.OK);
    }
    // http://localhost:2019/population/max
    @GetMapping(value = "/population/max", produces = {"application/json"})
    public ResponseEntity<?> displayPopulationLargest() {
        List<Country> myList = new ArrayList<>();

        countryrepos.findAll().iterator().forEachRemaining(myList::add);

        myList.sort(Comparator.comparing(Country::getPopulation).reversed());
        return new ResponseEntity<>(myList.get(0), HttpStatus.OK);
    }
}
