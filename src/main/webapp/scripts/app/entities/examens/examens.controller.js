'use strict';

angular.module('cmsApp')
    .controller('ExamensController', function ($scope, Examens, ExamensSearch) {
        $scope.examenss = [];
        $scope.loadAll = function() {
            Examens.query(function(result) {
               $scope.examenss = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Examens.get({id: id}, function(result) {
                $scope.examens = result;
                $('#deleteExamensConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Examens.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteExamensConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ExamensSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.examenss = result;
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
            $scope.examens = {type: null, libelle: null, date: null, id: null};
        };
    });
