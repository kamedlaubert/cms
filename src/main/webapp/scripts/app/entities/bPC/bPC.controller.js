'use strict';

angular.module('cmsApp')
    .controller('BPCController', function ($scope, BPC, BPCSearch) {
        $scope.bPCs = [];
        $scope.loadAll = function() {
            BPC.query(function(result) {
               $scope.bPCs = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            BPC.get({id: id}, function(result) {
                $scope.bPC = result;
                $('#deleteBPCConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            BPC.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteBPCConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            BPCSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.bPCs = result;
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
            $scope.bPC = {numeroBpc: null, objectBpc: null, id: null};
        };
    });
