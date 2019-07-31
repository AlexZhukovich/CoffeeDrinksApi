<?php
    class CoffeeException extends Exception { }

    class Coffee {
        private $_id;
        private $_title;
        private $_photoUrl;
        private $_description;
        private $_ingredients;
        private $_isFavourite;

        public function __construct($id, $title, $photoUrl, $description, $ingredients, $isFavourite) {
            $this->setId($id);
            $this->setTitle($title);
            $this->setPhotoUrl($photoUrl);
            $this->setDescription($description);
            $this->setIngredients($ingredients);
            $this->setFavourite($isFavourite);
        }

        public function getCoffeeArray() {
            $coffee = array();
            $coffee['id'] = $this->getId();
            $coffee['title'] = $this->getTitle();
            $coffee['photo_url'] = $this->getPhotoUrl();
            $coffee['description'] = $this->getDescription();
            $coffee['ingredients'] = $this->getIngredients();
            $coffee['favourite'] = $this->isFavourite();
            return $coffee;
        }

        public function getId() {
            return $this->_id;
        }

        public function setId($id) {
            if (($id !== null) && (!is_numeric($id) || ($id <= 0))) {
                throw new CoffeeException("Coffee ID error");
            }
            $this->_id = $id;
        }

        public function getTitle() {
            return $this->_title;
        }

        public function setTitle($title) {
            if (strlen($title) < 0 || strlen($title) > 255) {
                throw new CoffeeException("Coffee title error");
            }
            $this->_title = $title;
        }

        public function getPhotoUrl() {
            return $this->_photoUrl;
        }

        public function setPhotoUrl($photoUrl) {
            if (($photoUrl !== null) && (strlen($photoUrl) > 255)) {
                throw new CoffeeException("Coffee photo url error");
            }
            $this->_photoUrl = $photoUrl;
        }

        public function getDescription() {
            return $this->_description;
        }

        public function setDescription($description) {
            if (($description !== null) && (strlen($description) > 16777215)) {
                throw new CoffeeException("Coffee description error");
            }
            $this->_description = $description;
        }

        public function getIngredients() {
            return $this->_ingredients;
        }

        public function setIngredients($ingredients) {
            if (($ingredients !== null) && (strlen($ingredients) > 255)) {
                throw new CoffeeException("Coffee ingredients error");
            }
            $this->_ingredients = $ingredients;
        }

        public function isFavourite() {
            return $this->_isFavourite;
        }

        public function setFavourite($isFavourite) {
            if (strtoupper($isFavourite) !== 'Y' && strtoupper($isFavourite) !== 'N') {
                throw new CoffeeException("Coffee favourite must be Y or N");
            }
            $this->_isFavourite = $isFavourite; 
        }
    }
?>