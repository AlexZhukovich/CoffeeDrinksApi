<?php
    require_once('../config/ServerConfig.php');

    class Response {
        const STATUS_CODE_LABEL = 'statusCode';
        const SUCCESS_LABEL = 'success';
        const MESSAGE_LABEL = 'messages';
        const DATA_LABEL = 'data';

        private $_success;
        private $_httpStatusCode;
        private $_messages = array();
        private $_data;
        private $_toCache = false;
        private $_responseData = array();

        public function setSuccess($success) {
            $this->_success = $success;
        }

        public function setHttpStatusCode($httpStatusCode) {
            $this->_httpStatusCode = $httpStatusCode;
        }

        public function addMessage($message) {
            $this->_messages[] = $message;
        }

        public function setData($data) {
            $this->_data = $data;
        }

        public function toCache($toCache) {
            $this->_toCache = $toCache;
        }

        public function send() {
            header(ServerConfig::HEADER_CONTENT_TYPE);
            if ($this->_toCache == true) {
                header(ServerConfig::HEADER_CACHE);
            } else {
                header(ServerConfig::HEADER_NO_CACHE);
            }

            if (($this->_success !== false && $this->_success !== true) || !is_numeric($this->_httpStatusCode)) {
                http_response_code(500);
                $this->_responseData[Response::STATUS_CODE_LABEL] = 500;
                $this->_responseData[Response::SUCCESS_LABEL] = false;
                $this->addMessage("Response creation error");
                $this->_responseData[Response::MESSAGE_LABEL] = $this->_message;
            } else {
                http_response_code($this->_httpStatusCode);
                $this->_responseData[Response::STATUS_CODE_LABEL] = $this->_httpStatusCode;
                $this->_responseData[Response::SUCCESS_LABEL] = $this->_success;
                $this->_responseData[Response::MESSAGE_LABEL] = $this->_messages;
                $this->_responseData[Response::DATA_LABEL] = $this->_data;
            }

            echo json_encode($this->_responseData);
        }
    }
?>