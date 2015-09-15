'use strict';

angular.module('cmsApp')
    .controller('PatientController', function ($scope, Patient, PatientSearch) {
        $scope.patients = [];
        $scope.loadAll = function() {
            Patient.query(function(result) {
               $scope.patients = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Patient.get({id: id}, function(result) {
                $scope.patient = result;
                $('#deletePatientConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Patient.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePatientConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            PatientSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.patients = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.patient = {nom: null, prenom: null, dateNaissance: null, id: null};
        };
    });
