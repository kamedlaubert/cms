'use strict';

angular.module('cmsApp')
    .controller('ConsultationController', function ($scope, Consultation, ConsultationSearch) {
        $scope.consultations = [];
        $scope.loadAll = function() {
            Consultation.query(function(result) {
               $scope.consultations = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Consultation.get({id: id}, function(result) {
                $scope.consultation = result;
                $('#deleteConsultationConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Consultation.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteConsultationConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ConsultationSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.consultations = result;
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
            $scope.consultation = {motif: null, date: null, diagnostiqueConsul: null, id: null};
        };
    });
