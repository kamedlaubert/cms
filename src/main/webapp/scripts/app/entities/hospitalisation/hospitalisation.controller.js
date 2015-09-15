'use strict';

angular.module('cmsApp')
    .controller('HospitalisationController', function ($scope, Hospitalisation, HospitalisationSearch) {
        $scope.hospitalisations = [];
        $scope.loadAll = function() {
            Hospitalisation.query(function(result) {
               $scope.hospitalisations = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Hospitalisation.get({id: id}, function(result) {
                $scope.hospitalisation = result;
                $('#deleteHospitalisationConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Hospitalisation.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteHospitalisationConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            HospitalisationSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.hospitalisations = result;
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
            $scope.hospitalisation = {code: null, dateAdmission: null, dateSorti: null, motif: null, bilan: null, id: null};
        };
    });
