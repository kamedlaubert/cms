'use strict';

angular.module('cmsApp').controller('MedicamentDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Medicament', 'Ordonnance',
        function($scope, $stateParams, $modalInstance, entity, Medicament, Ordonnance) {

        $scope.medicament = entity;
        $scope.ordonnances = Ordonnance.query();
        $scope.load = function(id) {
            Medicament.get({id : id}, function(result) {
                $scope.medicament = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('cmsApp:medicamentUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.medicament.id != null) {
                Medicament.update($scope.medicament, onSaveFinished);
            } else {
                Medicament.save($scope.medicament, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
