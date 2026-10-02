# Asteroid Impact Simulator

A Java desktop application for simulating the atmospheric entry and impact of an asteroid at a selected location on Earth.

The application allows the user to configure asteroid parameters, select an impact location on an interactive satellite map, simulate the asteroid's trajectory through the atmosphere, and visualize the resulting impact zones directly on the map.

The simulation combines a fourth-order Runge-Kutta numerical integration method with an impact-crater scaling model based on Holsapple (1993).

<br />

## Features

* Configure asteroid diameter from **10 to 2000 m**
* Configure entry velocity from **10 to 70 km/s**
* Configure entry angle from **5 to 90 degrees**
* Select asteroid composition:

  * Stone
  * Iron
  * Ice
* Select an impact location by clicking directly on the world map
* Interactive satellite imagery based on **ESRI World Imagery**
* Geographic labels using **ESRI Reference**
* Numerical atmospheric-entry simulation using **Runge-Kutta 4th order**
* Atmospheric drag and mass loss due to ablation
* Calculation of kinetic energy at impact
* Calculation of crater radius
* Calculation of four impact-effect zones
* Automatic map view selection based on the simulated impact radius
* Detailed numerical results displayed in the application
* Simulation reset functionality for running another scenario

<br />

## How It Works

### 1. Asteroid Initialization

The asteroid is modeled as a homogeneous sphere.

Its initial mass is calculated from its volume and selected material density using $m_0 = \rho \cdot \frac{4}{3}\pi r_0^3$.

where:

* $m_0$ is the initial mass
* $\rho$ is the asteroid density
* $r_0$ is the initial radius
* $r_0 = d / 2$

The initial kinetic energy is calculated using $E_0 = \frac{1}{2}m_0v_0^2$.

The initial velocity is converted from km/s to m/s before being used in the simulation.


### 2. Asteroid Composition

Three asteroid compositions are supported:

| Composition |    Density |
| ----------- | ---------: |
| Stone       | 2700 kg/m³ |
| Iron        | 7800 kg/m³ |
| Ice         |  900 kg/m³ |

The selected density affects the asteroid's initial mass as well as the crater-scaling coefficient used later in the simulation.


### 3. Atmospheric Simulation

The atmospheric entry is simulated numerically using the **fourth-order Runge-Kutta method (RK4)**.

The simulation state is represented by the vector $\mathbf{x}(t) = [h(t), v(t), m(t)]$, where:

* $h$ is altitude
* $v$ is velocity along the trajectory
* $m$ is current asteroid mass

The integration step is $\Delta t = 0.5\ \text{s}$.

### 4. Atmospheric Density

Atmospheric density is approximated using an isothermal barometric model:

$\rho_{\text{air}}(h) = \rho_0 e^{-h/H_{\text{scale}}}$.

The model uses $\rho_0 = 1.225\ \text{kg/m}^3$ as the air density at sea level and $H_{\text{scale}} = 8500\ \text{m}$ as the atmospheric scale height.

### 5. Atmospheric Drag

The aerodynamic drag force is calculated as $F_d = \frac{1}{2}\rho_{\text{air}}v^2C_dA$.

where:

* $\rho_{\text{air}}$ is atmospheric density
* $v$ is asteroid velocity
* $C_d = 1.0$ is the drag coefficient
* $A$ is the asteroid's current cross-sectional area

The asteroid radius and cross-sectional area are recalculated from its current mass during the simulation.

### 6. Ablation

The asteroid loses mass as it travels through the atmosphere.

The simplified ablation model uses $\frac{dm}{dt} = -\frac{C_H\rho_{\text{air}}v^3A}{Q_{\text{ab}}}$.

The model uses $C_H = 0.1$ and $Q_{\text{ab}} = 8 \times 10^6\ \text{J/kg}$.

For numerical stability, the asteroid mass is not allowed to fall below $1\ \text{kg}$.

### 7. Equations of Motion

The primary equations used during atmospheric entry are $\frac{dh}{dt} = -v\sin(\theta)$ and $\frac{dv}{dt} = -\frac{F_d}{m} - g\sin(\theta)$.

The gravitational acceleration is approximated as $g \approx 9.81\ \text{m/s}^2$.

The trajectory variables are integrated using RK4, while the simplified ablation equation is integrated using Euler's method.

### 8. Energy Deposited in the Atmosphere

During each integration step, the energy absorbed by the atmosphere through aerodynamic drag is accumulated using $E_{\text{atm}} \approx \sum F_d v,\Delta t$.

The percentage of the initial kinetic energy absorbed by the atmosphere is calculated as $E_{\text{atm}}[%] = \frac{E_{\text{atm}}}{E_0}\cdot100%$.

### 9. Impact Energy

After atmospheric integration is complete, the remaining kinetic energy is calculated as $E_{\text{impact}} = \frac{1}{2}m_{\text{final}}v_{\text{final}}^2$.

The result is converted into megatons of TNT using $E_{\text{Mt}} = \frac{E_{\text{impact}}}{Y_{\text{TNT}}\times10^6}$.

The TNT conversion constant is $Y_{\text{TNT}} = 4.184 \times 10^9\ \text{J/t}$.

### 10. Crater Calculation

The crater radius is estimated using an energy-scaling model based on **Holsapple (1993)**.

The scaling coefficient depends on asteroid density:

| Density           | Coefficient | Material              |
| ----------------- | ----------: | --------------------- |
| `< 1500 kg/m³`    |       0.011 | Ice / porous material |
| `1500–4999 kg/m³` |      0.0133 | Rock                  |
| `≥ 5000 kg/m³`    |       0.016 | Metal                 |

The initial crater estimate is calculated using $R_{\text{est}} = \alpha E_{\text{impact}}^{0.294}$.

The estimate is subsequently verified using numerical integration of pressure inside the impacted ground volume.

The simulation uses `1000` integration steps and a pressure threshold of `1 GPa`.

### 11. Impact Zones

The application calculates four zones around the impact point.

The zone radius follows the general scaling relationship $R = kE_{\text{Mt}}^{1/3}$.

The resulting zones are:

| Zone              | Coefficient | Overpressure threshold | Typical effect                               |
| ----------------- | ----------: | ---------------------: | -------------------------------------------- |
| Total destruction |        0.28 |              > 138 kPa | Crater and complete destruction of buildings |
| Heavy damage      |        0.80 |               > 35 kPa | Collapse of weaker structures                |
| Broken windows    |         5.0 |                > 3 kPa | Broken windows and light damage              |
| Shock wave        |        12.0 |                > 1 kPa | People knocked down and light damage         |

These zones are visualized directly on the map as concentric areas centered on the selected impact location.

<br />

## Simulation Results

The simulation produces an `ImpactResult` containing the main calculated parameters:

| Field            | Description                                        |
| ---------------- | -------------------------------------------------- |
| `E_MT`           | Impact energy in megatons of TNT                   |
| `craterKm`       | Crater radius in kilometers                        |
| `r_total`        | Total destruction radius                           |
| `r_heavy`        | Heavy damage radius                                |
| `r_glass`        | Broken-window radius                               |
| `r_wave`         | Shock-wave radius                                  |
| `mapView`        | Automatically selected map-view category           |
| `massLostPct`    | Percentage of asteroid mass lost in the atmosphere |
| `energyAtmPct`   | Percentage of energy absorbed by the atmosphere    |
| `maxPressureKPa` | Maximum dynamic pressure                           |

<br />

## Limitations

The simulation is an educational and computational model and uses several physical simplifications:

* The atmosphere is represented using a simplified isothermal barometric model rather than a multilayer atmospheric model.
* The asteroid does not split into multiple fragments, even when dynamic pressure becomes very high.
* Every asteroid is modeled as a sphere. Real meteoroids and asteroids may have irregular shapes that affect drag and ablation.
* The simulation does not account for trajectory curvature caused by Earth's gravity.
* The crater model assumes a rocky target. Impacts in oceans are not modeled and therefore do not simulate tsunami effects.
* The drag coefficient is fixed at $C_d = 1.0$, while real aerodynamic behavior depends on factors such as Mach number and object shape.

<br />

## Requirements

To build and run the project, the following are required:

* Java **21 or newer**
* Maven
* Internet access while the application is running because map tiles are loaded from ESRI services

<br />

## Running the Application

Clone the repository:

```bash
git clone https://github.com/anawooj/asteroid-simulation.git
cd asteroid-simulation
```

Build and run the project:

```bash
mvn clean compile exec:java
```

<br />

## Video Demonstration

<p align="center">
  to be added!
</p>

## Disclaimer

This project is a numerical simulation and visualization tool based on simplified physical models. Its results should not be interpreted as precise predictions of real asteroid impacts.

Several physical phenomena, including fragmentation, realistic atmospheric layering, trajectory curvature, irregular asteroid geometry, and ocean impacts, are intentionally simplified or omitted from the current model.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for the full license text.
