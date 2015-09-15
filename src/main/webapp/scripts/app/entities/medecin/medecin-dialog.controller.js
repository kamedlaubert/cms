'use strict';

angular.module('cmsApp').controller('MedecinDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Medecin',
        function($scope, $stateParams, $modalInstance, entity, Medecin) {

        $scope.medecin = entity;
        $scope.load = function(id) {
            Medecin.get({id : id}, function(result) {
                $scope.medecin = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('cmsApp:medecinUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.medecin.id != null) {
                Medecin.update($scope.medecin, onSaveFinished);
            } else {
                Medecin.save($scope.medecin, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
