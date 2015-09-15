'use strict';

angular.module('cmsApp').controller('OrdonnanceDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Ordonnance', 'Consultation', 'Medicament',
        function($scope, $stateParams, $modalInstance, entity, Ordonnance, Consultation, Medicament) {

        $scope.ordonnance = entity;
        $scope.consultations = Consultation.query();
        $scope.medicaments = Medicament.query();
        $scope.load = function(id) {
            Ordonnance.get({id : id}, function(result) {
                $scope.ordonnance = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('cmsApp:ordonnanceUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.ordonnance.id != null) {
                Ordonnance.update($scope.ordonnance, onSaveFinished);
            } else {
                Ordonnance.save($scope.ordonnance, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
